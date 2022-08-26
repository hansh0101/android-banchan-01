package co.kr.woowahan_banchan.presentation.viewmodel.productdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.woowahan_banchan.domain.entity.detail.DishInfo
import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity
import co.kr.woowahan_banchan.domain.usecase.*
import co.kr.woowahan_banchan.presentation.viewmodel.UiEvent
import co.kr.woowahan_banchan.presentation.viewmodel.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productDetailUseCase: ProductDetailUseCase,
    private val cartAddUseCase: CartAddUseCase,
    private val historyAddUseCase: HistoryAddUseCase,
    private val getCartItemCountUseCase: GetCartItemCountUseCase,
    private val checkOrderCompleteUseCase: CheckOrderCompleteUseCase
) : ViewModel() {
    // State
    private val _dishInfo = MutableStateFlow<UiState<DishInfo>>(UiState.Init)
    val dishInfo: StateFlow<UiState<DishInfo>> get() = _dishInfo
    private val _amount = MutableStateFlow<Int>(1)
    val amount: StateFlow<Int> get() = _amount
    private val _cartCount = MutableStateFlow(0)
    val cartCount: StateFlow<Int> get() = _cartCount
    private val _isOrderCompleted = MutableStateFlow<UiState<Boolean>>(UiState.Init)
    val isOrderCompleted: StateFlow<UiState<Boolean>> get() = _isOrderCompleted

    // Event
    private val _cartAddEvent = MutableSharedFlow<UiEvent<Unit>>()
    val cartAddEvent: SharedFlow<UiEvent<Unit>> get() = _cartAddEvent

    init {
        getCartItemCount()
        checkOrderComplete()
    }

    fun fetchUiState(hash: String) {
        viewModelScope.launch {
            productDetailUseCase(hash)
                .onSuccess { _dishInfo.value = UiState.Success(it) }
                .onFailure {
                    _dishInfo.value = when (it as ErrorEntity) {
                        is ErrorEntity.RetryableError -> UiState.Error("문제가 생겼어요! 다시 시도해보시겠어요?")
                        is ErrorEntity.ConditionalError -> UiState.Error("인터넷 연결에 문제가 있어요!")
                        is ErrorEntity.UnknownError -> UiState.Error("앗! 문제가 발생했어요!")
                    }
                }
        }
    }

    fun addToHistory(hash: String, name: String) {
        viewModelScope.launch {
            historyAddUseCase(hash, name)
                .onFailure { Timber.e(it) }
        }
    }

    fun addToCart(hash: String, name: String) {
        viewModelScope.launch {
            cartAddUseCase(hash, amount.value, name)
                .onSuccess { _cartAddEvent.emit(UiEvent.Success(Unit)) }
                .onFailure { _cartAddEvent.emit(UiEvent.Error(it as ErrorEntity)) }
        }
    }

    fun updateAmount(isPlus: Boolean) {
        if (isPlus) {
            _amount.value = amount.value + 1
        } else {
            if (amount.value != 1) {
                _amount.value = amount.value - 1
            }
        }
    }

    private fun getCartItemCount() {
        viewModelScope.launch {
            getCartItemCountUseCase()
                .collect { result ->
                    result.onSuccess {
                        _cartCount.value = it
                    }.onFailure {
                        _cartCount.value = 0
                    }
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