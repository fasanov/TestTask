package app.electronics.com.testtask.di

import app.electronics.com.testtask.data.repository.CuratedPhotosRepositoryImpl
import app.electronics.com.testtask.data.source.local.CuratedPhotosDatabase
import app.electronics.com.testtask.data.source.network.CuratedPhotosNetworkDataSource
import app.electronics.com.testtask.data.source.network.NetworkDataSource
import app.electronics.com.testtask.data.source.network.PexelsApiService
import app.electronics.com.testtask.domain.repository.CuratedPhotosRepository
import app.electronics.com.testtask.domain.usecase.GetCuratedPhotosUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideCuratedPhotosNetworkDataSource(service: PexelsApiService): NetworkDataSource {
        return CuratedPhotosNetworkDataSource(service)
    }

    @Provides
    fun provideCuratedPhotosRepository(
        database: CuratedPhotosDatabase,
        dataSource: NetworkDataSource,
    ): CuratedPhotosRepository {
        return CuratedPhotosRepositoryImpl(database, dataSource)
    }

    @Provides
    fun provideCuratedPhotosUseCase(repository: CuratedPhotosRepository): GetCuratedPhotosUseCase {
        return GetCuratedPhotosUseCase(repository)
    }
}