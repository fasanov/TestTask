package app.electronics.com.testtask.data.source.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PexelsApiService {

    @GET("v1/curated")
    suspend fun getCuratedPhotos(
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
    ): Response<NetworkCuratedPhotos>
}