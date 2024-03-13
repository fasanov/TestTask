package app.electronics.com.testtask.di

import android.content.Context
import androidx.room.Room
import app.electronics.com.testtask.data.source.local.CuratedPhotosDao
import app.electronics.com.testtask.data.source.local.CuratedPhotosDatabase
import app.electronics.com.testtask.data.source.local.RemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): CuratedPhotosDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CuratedPhotosDatabase::class.java,
            "CuratedPhotos.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideCuratedPhotosDao(database: CuratedPhotosDatabase): CuratedPhotosDao =
        database.getCuratedPhotoDao()

    @Singleton
    @Provides
    fun provideRemoteKeysDao(database: CuratedPhotosDatabase): RemoteKeysDao =
        database.getRemoteKeysDao()
}
