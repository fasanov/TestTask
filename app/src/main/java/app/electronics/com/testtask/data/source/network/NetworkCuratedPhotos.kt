package app.electronics.com.testtask.data.source.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkCuratedPhotos(
    @Json(name = "photos") val photos: List<NetworkCuratedPhoto>,
    @Json(name = "per_page") val perPage: Int = -1,
) {
    val endOfPaginationReached = perPage == 0
}

@JsonClass(generateAdapter = true)
data class NetworkCuratedPhoto(
    @Json(name = "id") val id: Int,
    @Json(name = "photographer") val photographerName: String,
    @Json(name = "src") val src: NetworkPhotoSrc,
    @Json(name = "alt") val description: String,
)

@JsonClass(generateAdapter = true)
data class NetworkPhotoSrc(
    @Json(name = "medium") val photo: String,
)