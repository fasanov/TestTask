package app.electronics.com.testtask.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import app.electronics.com.testtask.data.source.local.CuratedPhotosDatabase
import app.electronics.com.testtask.data.source.local.LocalCuratedPhoto
import app.electronics.com.testtask.data.source.local.RemoteKeys
import app.electronics.com.testtask.data.source.network.NetworkCuratedPhoto
import app.electronics.com.testtask.data.source.network.NetworkDataSource
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class CuratedPhotoRemoteMediator(
    private val curatedPhotosDatabase: CuratedPhotosDatabase,
    private val curatedPhotosNetworkDataSource: NetworkDataSource,
) : RemoteMediator<Int, LocalCuratedPhoto>() {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        Timber.d("initialize(): cacheTimeout=$cacheTimeout")

        return if (System.currentTimeMillis() - (curatedPhotosDatabase.getRemoteKeysDao()
                .getCreationTime() ?: 0) < cacheTimeout
        ) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, LocalCuratedPhoto>): RemoteKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { photo ->
            curatedPhotosDatabase.getRemoteKeysDao().getRemoteKeyByPhotoId(photo.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, LocalCuratedPhoto>): RemoteKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { photo ->
            curatedPhotosDatabase.getRemoteKeysDao().getRemoteKeyByPhotoId(photo.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, LocalCuratedPhoto>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                curatedPhotosDatabase.getRemoteKeysDao().getRemoteKeyByPhotoId(id)
            }
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocalCuratedPhoto>
    ): MediatorResult {
        Timber.d("load(): loadType=$loadType")
        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)

                val prevKey = remoteKeys?.prevKey
                prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)

                val nextKey = remoteKeys?.nextKey
                nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        try {
            val photosResponse =
                curatedPhotosNetworkDataSource.requestCuratedPhotos(state.config.pageSize, page)

            val endOfPaginationReached = photosResponse.endOfPaginationReached
            val photos = photosResponse.photos
            Timber.d("load(): photosSize=${photos.size}, endOfPaginationReached=$endOfPaginationReached")

            curatedPhotosDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    curatedPhotosDatabase.getRemoteKeysDao().clearRemoteKeys()
                    curatedPhotosDatabase.getCuratedPhotoDao().clearAllCuratedPhotos()
                }
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (endOfPaginationReached) null else page + 1
                val remoteKeys = photos.map {
                    RemoteKeys(id = it.id, prevKey = prevKey, currentPage = page, nextKey = nextKey)
                }

                curatedPhotosDatabase.getRemoteKeysDao().insertAll(remoteKeys)
                curatedPhotosDatabase.getCuratedPhotoDao()
                    .insertAll(photos.toLocalCuratedPhoto(page))
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (error: IOException) {
            return MediatorResult.Error(error)
        } catch (error: HttpException) {
            return MediatorResult.Error(error)
        }
    }
}

private fun List<NetworkCuratedPhoto>.toLocalCuratedPhoto(page: Int): List<LocalCuratedPhoto> {
    return map {
        LocalCuratedPhoto(
            id = it.id,
            photographerName = it.photographerName,
            url = it.src.photo,
            description = it.description,
            page = page
        )
    }
}