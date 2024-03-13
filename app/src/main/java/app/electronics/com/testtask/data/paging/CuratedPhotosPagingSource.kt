package app.electronics.com.testtask.data.paging

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.electronics.com.testtask.domain.model.CuratedPhoto

class CuratedPhotosPagingSource(
    private val requestCuratedPhotos: suspend (perPage: Int, page: Int) -> List<CuratedPhoto>,
) : PagingSource<Int, CuratedPhoto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CuratedPhoto> {
        return try {
            val page = params.key ?: 1
            val curatedPhotos = requestCuratedPhotos(params.loadSize, page)
            PagingData
            LoadResult.Page(
                data = curatedPhotos,
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (curatedPhotos.isEmpty()) null else page.plus(1),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CuratedPhoto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}