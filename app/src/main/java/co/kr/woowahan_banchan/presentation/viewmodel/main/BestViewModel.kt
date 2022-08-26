package co.kr.woowahan_banchan.presentation.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.woowahan_banchan.domain.entity.dish.BestItem
import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity
import co.kr.woowahan_banchan.domain.usecase.GetBestsUseCase
import co.kr.woowahan_banchan.presentation.viewmodel.UiEvent
import co.kr.woowahan_banchan.presentation.viewmodel.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BestViewModel @Inject constructor(
    private val getBestsUseCase: GetBestsUseCase
) : ViewModel() {
    private val _bestItems = MutableStateFlow<UiState<List<BestItem>>>(UiState.Init)
    val bestItems: StateFlow<UiState<List<BestItem>>> get() = _bestItems
    private val _bestItemsEvent = MutableSharedFlow<UiEvent<Unit>>()
    val bestItemsEvent: SharedFlow<UiEvent<Unit>> get() = _bestItemsEvent
    private var collectJob: Job? = null

    init {
        getBests()
    }

    fun getBests() {
        if (collectJob == null) {
            collectJob = viewModelScope.launch {
                getBestsUseCase()
                    .collect { result ->
                        result.onSuccess { _bestItems.value = UiState.Success(it) }
                            .onFailure {
                                _bestItems.value = UiState.Error(it.message)
                                _bestItemsEvent.emit(UiEvent.Error(it as ErrorEntity))
                            }
                    }
            }
        }
    }

    fun cancelCollectJob() {
        collectJob?.cancel()
        collectJob = null
    }

    fun reFetchBests() {
        cancelCollectJob()
        getBests()
    }
}