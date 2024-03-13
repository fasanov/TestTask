package app.electronics.com.testtask.data.source.network

interface NetworkDataSource {
    suspend fun requestCuratedPhotos(perPage: Int, page: Int): NetworkCuratedPhotos
}
