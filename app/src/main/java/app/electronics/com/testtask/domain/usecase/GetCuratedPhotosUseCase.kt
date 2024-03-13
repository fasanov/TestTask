package app.electronics.com.testtask.domain.usecase

import androidx.paging.PagingData
import app.electronics.com.testtask.data.source.local.LocalCuratedPhoto
import app.electronics.com.testtask.domain.repository.CuratedPhotosRepository
import kotlinx.coroutines.flow.Flow

class GetCuratedPhotosUseCase(
    private val repository: CuratedPhotosRepository,
) {

    operator fun invoke(): Flow<PagingData<LocalCuratedPhoto>> {
        return repository.getCuratedPhotos()
    }
}