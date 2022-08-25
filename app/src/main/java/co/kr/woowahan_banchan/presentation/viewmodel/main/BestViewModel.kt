package co.kr.woowahan_banchan.presentation.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.woowahan_banchan.domain.entity.dish.BestItem
import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity
import co.kr.woowahan_banchan.domain.usecase.GetBestsUseCase
import co.kr.woowahan_banchan.presentation.viewmodel.UiEvents
import co.kr.woowahan_banchan.presentation.viewmodel.UiState
import co.kr.woowahan_banchan.presentation.viewmodel.UiStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BestViewModel @Inject constructor(
    private val getBestsUseCase: GetBestsUseCase
) : ViewModel() {
    private val _bestItems = MutableStateFlow<UiStates<List<BestItem>>>(UiStates.Init)
    val bestItems: StateFlow<UiStates<List<BestItem>>> get() = _bestItems
    private val _bestItemsEvent = MutableSharedFlow<UiEvents<Unit>>()
    val bestItemsEvent: SharedFlow<UiEvents<Unit>> get() = _bestItemsEvent
    private var collectJob: Job? = null

    init {
        getBests()
    }

    private fun getBests() {
        collectJob = viewModelScope.launch {
            getBestsUseCase()
                .collect { result ->
                    result.onSuccess { _bestItems.value = UiStates.Success(it) }
                        .onFailure {
                            _bestItems.value = UiStates.Error(it.message)
                            _bestItemsEvent.emit(UiEvents.Error(it as ErrorEntity))
                        }
                }
        }
    }

    fun reFetchBests() {
        collectJob?.cancel()
        getBests()
    }
}