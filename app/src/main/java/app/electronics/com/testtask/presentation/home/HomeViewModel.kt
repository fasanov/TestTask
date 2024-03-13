package app.electronics.com.testtask.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import app.electronics.com.testtask.data.source.local.LocalCuratedPhoto
import app.electronics.com.testtask.domain.model.CuratedPhoto
import app.electronics.com.testtask.domain.usecase.GetCuratedPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCuratedPhotosUseCase: GetCuratedPhotosUseCase,
) : ViewModel() {

    private val _curatedPhotosState = MutableStateFlow(PagingData.empty<CuratedPhoto>())
    val curatedPhotosState get() = _curatedPhotosState

    init {
        getCuratedPhotos()
    }

    private fun getCuratedPhotos() {
        Timber.d("getCuratedPhotos()")
        getCuratedPhotosUseCase()
            .distinctUntilChanged()
            .map { pagingData ->
                pagingData.map { it.toCuratedPhoto() }
            }
            .cachedIn(viewModelScope)
            .onEach {
                _curatedPhotosState.emit(it)
            }
            .launchIn(viewModelScope)
    }
}

private fun LocalCuratedPhoto.toCuratedPhoto(): CuratedPhoto {
    return CuratedPhoto(
        id = id,
        photographerName = photographerName,
        url = url,
        description = description
    )
}
