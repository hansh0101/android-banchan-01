package co.kr.woowahan_banchan.presentation.viewmodel.productdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.woowahan_banchan.domain.entity.detail.DishInfo
import co.kr.woowahan_banchan.domain.usecase.ProductDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productDetailUseCase: ProductDetailUseCase
) : ViewModel() {
    private val _dishInfo = MutableStateFlow<UiState>(UiState.Init)
    val dishInfo: StateFlow<UiState> get() = _dishInfo
    private val _amount = MutableStateFlow<Int>(1)
    val amount: StateFlow<Int> get() = _amount

    fun fetchUiState(hash: String) {
        viewModelScope.launch {
            productDetailUseCase(hash)
                .onSuccess { _dishInfo.value = UiState.Success(it) }
                .onFailure { _dishInfo.value = UiState.Error(it.message) }
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

    sealed class UiState {
        object Init : UiState()
        data class Success(val dishInfo: DishInfo) : UiState()
        data class Error(val message: String?) : UiState()
    }
}