package app.electronics.com.testtask.domain.repository

import androidx.paging.PagingData
import app.electronics.com.testtask.data.source.local.LocalCuratedPhoto
import kotlinx.coroutines.flow.Flow

interface CuratedPhotosRepository {
    fun getCuratedPhotos(): Flow<PagingData<LocalCuratedPhoto>>
}