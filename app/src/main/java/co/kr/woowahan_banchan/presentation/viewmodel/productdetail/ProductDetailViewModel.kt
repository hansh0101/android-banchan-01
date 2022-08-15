package co.kr.woowahan_banchan.presentation.viewmodel.productdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.woowahan_banchan.domain.entity.detail.DishInfo
import co.kr.woowahan_banchan.domain.usecase.CartAddUseCase
import co.kr.woowahan_banchan.domain.usecase.HistoryAddUseCase
import co.kr.woowahan_banchan.domain.usecase.ProductDetailUseCase
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
    private val historyAddUseCase: HistoryAddUseCase
) : ViewModel() {
    private val _dishInfo = MutableStateFlow<UiState<DishInfo>>(UiState.Init)
    val dishInfo: StateFlow<UiState<DishInfo>> get() = _dishInfo
    private val _amount = MutableStateFlow<Int>(1)
    val amount: StateFlow<Int> get() = _amount
    private val _isAddSuccess = MutableSharedFlow<Boolean>()
    val isAddSuccess: SharedFlow<Boolean> get() = _isAddSuccess

    fun fetchUiState(hash: String) {
        viewModelScope.launch {
            productDetailUseCase(hash)
                .onSuccess { _dishInfo.value = UiState.Success(it) }
                .onFailure { _dishInfo.value = UiState.Error(it.message) }
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
                .onSuccess { _isAddSuccess.emit(true) }
                .onFailure { _isAddSuccess.emit(false) }
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
}