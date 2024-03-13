package app.electronics.com.testtask.data.source.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class CuratedPhotosNetworkDataSource(
    private val pexelsApiService: PexelsApiService,
) : NetworkDataSource {

    override suspend fun requestCuratedPhotos(perPage: Int, page: Int): NetworkCuratedPhotos {
        return withContext(Dispatchers.IO) {
            try {
                Timber.d("requestCuratedPhotos()")
                val response = pexelsApiService.getCuratedPhotos(perPage, page)
                val body = response.body()

                if (!response.isSuccessful) {
                    throw WebPexelsException(response.code(), response.message())
                }
                if (body == null) {
                    throw EmptyPexelsException()
                }
                Timber.d("requestCuratedPhotos(): photosSize=${body.photos.size}")
                body
            } catch (t: Throwable) {
                t.printStackTrace()
                NetworkCuratedPhotos(emptyList())
            }
        }
    }
}