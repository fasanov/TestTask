package app.electronics.com.testtask.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.electronics.com.testtask.data.paging.CuratedPhotoRemoteMediator
import app.electronics.com.testtask.data.source.local.CuratedPhotosDatabase
import app.electronics.com.testtask.data.source.local.LocalCuratedPhoto
import app.electronics.com.testtask.data.source.network.NetworkDataSource
import app.electronics.com.testtask.domain.repository.CuratedPhotosRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class CuratedPhotosRepositoryImpl(
    private val curatedPhotosDatabase: CuratedPhotosDatabase,
    private val curatedPhotosNetworkDataSource: NetworkDataSource,
) : CuratedPhotosRepository {

    override fun getCuratedPhotos(): Flow<PagingData<LocalCuratedPhoto>> {
        Timber.d("getCuratedPhotos()")
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
            ),
            pagingSourceFactory = {
                curatedPhotosDatabase.getCuratedPhotoDao().getCuratedPhotos()
            },
            remoteMediator = CuratedPhotoRemoteMediator(
                curatedPhotosDatabase,
                curatedPhotosNetworkDataSource,
            )
        ).flow
    }

    companion object {
        private const val PAGE_SIZE = 20
        private const val PREFETCH_DISTANCE = 3
    }
}