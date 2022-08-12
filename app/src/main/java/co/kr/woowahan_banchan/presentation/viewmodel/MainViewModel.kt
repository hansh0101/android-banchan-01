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
    private val _uiState = MutableStateFlow<UiState>(UiState.Init)
    val uiState : StateFlow<UiState> get() = _uiState

    fun getBests() = viewModelScope.launch {
        getBestsUseCase().collect {
            _uiState.value = UiState.Success(it)
        }
    }

    sealed class UiState {
        object Init : UiState()
        data class Success(val list : List<BestItem>) :UiState()
        data class Error(val message : String) : UiState()
    }
}


