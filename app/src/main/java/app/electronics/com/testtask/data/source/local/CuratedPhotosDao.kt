package app.electronics.com.testtask.data.source.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CuratedPhotosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photos: List<LocalCuratedPhoto>)

    @Query("Select * From curated_photo Order By page")
    fun getCuratedPhotos(): PagingSource<Int, LocalCuratedPhoto>

    @Query("Delete From curated_photo")
    suspend fun clearAllCuratedPhotos()
}
