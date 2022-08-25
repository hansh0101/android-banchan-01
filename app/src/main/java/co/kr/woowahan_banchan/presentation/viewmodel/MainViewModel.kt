package co.kr.woowahan_banchan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.woowahan_banchan.domain.usecase.CheckOrderCompleteUseCase
import co.kr.woowahan_banchan.domain.usecase.GetCartItemCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCartItemCountUseCase: GetCartItemCountUseCase,
    private val checkOrderCompleteUseCase: CheckOrderCompleteUseCase
) : ViewModel() {

    private val _cartCount = MutableStateFlow(0)
    val cartCount: StateFlow<Int> get() = _cartCount

    private val _isOrderCompleted = MutableStateFlow<UiState<Boolean>>(UiState.Init)
    val isOrderCompleted: StateFlow<UiState<Boolean>> get() = _isOrderCompleted

    init {
        getCartItemCount()
        checkOrderComplete()
    }

    private fun getCartItemCount() {
        viewModelScope.launch {
            getCartItemCountUseCase()
                .catch { _cartCount.value = 0 }
                .collect { result ->
                    result.onSuccess { _cartCount.value = it }
                }
        }
    }

    private fun checkOrderComplete() {
        viewModelScope.launch {
            checkOrderCompleteUseCase().collect { result ->
                result.onSuccess {
                    _isOrderCompleted.value = UiState.Success(it)
                }.onFailure {
                    _isOrderCompleted.value = UiState.Error("배송 정보를 받아오지 못했어요!")
                }
            }
        }
    }
}