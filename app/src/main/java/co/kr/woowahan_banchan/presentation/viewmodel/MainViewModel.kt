package co.kr.woowahan_banchan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.woowahan_banchan.domain.entity.dish.BestItem
import co.kr.woowahan_banchan.domain.usecase.GetBestsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getBestsUseCase: GetBestsUseCase
) : ViewModel() {
    private val _bestItems = MutableStateFlow<UiState<List<BestItem>>>(UiState.Init)
    val bestItems: StateFlow<UiState<List<BestItem>>> get() = _bestItems

    fun getBests() = viewModelScope.launch {
        getBestsUseCase().collect {
            _bestItems.value = UiState.Success(it)
        }
    }

    sealed class UiState<out T> {
        object Init : UiState<Nothing>()
        data class Success<out T>(val data: T) : UiState<T>()
        data class Error(val message: String?) : UiState<Nothing>()
    }
}


