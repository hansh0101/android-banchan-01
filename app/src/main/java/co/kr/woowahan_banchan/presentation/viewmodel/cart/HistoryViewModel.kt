package co.kr.woowahan_banchan.presentation.viewmodel.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity
import co.kr.woowahan_banchan.domain.entity.history.HistoryItem
import co.kr.woowahan_banchan.domain.usecase.HistoryUseCase
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
class HistoryViewModel @Inject constructor(
    private val historyUseCase: HistoryUseCase
) : ViewModel() {
    private val _historyItems = MutableStateFlow<UiState<List<HistoryItem>>>(UiState.Init)
    val historyItems: StateFlow<UiState<List<HistoryItem>>> get() = _historyItems
    private val _historyItemsEvent = MutableSharedFlow<UiEvent<Unit>>()
    val historyItemsEvent: SharedFlow<UiEvent<Unit>> get() = _historyItemsEvent
    private var collectJob: Job? = null

    fun fetchHistoryItems() {
        collectJob = viewModelScope.launch {
            historyUseCase.invoke(previewMode = false)
                .collect { result ->
                    result.onSuccess { _historyItems.value = UiState.Success(it) }
                        .onFailure {
                            _historyItems.value = UiState.Error(it.message)
                            _historyItemsEvent.emit(UiEvent.Error(it as ErrorEntity))
                        }
                }
        }
    }

    fun reFetchHistoryItems() {
        collectJob?.cancel()
        fetchHistoryItems()
    }
}