package app.electronics.com.testtask.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "curated_photo")
data class LocalCuratedPhoto(
    @PrimaryKey val id: Int,
    val photographerName: String,
    val url: String,
    val description: String,
    var page: Int,
)
