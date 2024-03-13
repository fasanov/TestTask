package app.electronics.com.testtask.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [LocalCuratedPhoto::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class CuratedPhotosDatabase : RoomDatabase() {
    abstract fun getCuratedPhotoDao(): CuratedPhotosDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao
}
