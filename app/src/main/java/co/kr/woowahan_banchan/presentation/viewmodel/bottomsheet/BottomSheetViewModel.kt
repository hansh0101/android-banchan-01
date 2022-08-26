package co.kr.woowahan_banchan.presentation.viewmodel.bottomsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.woowahan_banchan.domain.entity.dish.SelectedDish
import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity
import co.kr.woowahan_banchan.domain.usecase.CartAddUseCase
import co.kr.woowahan_banchan.presentation.viewmodel.UiEvent
import co.kr.woowahan_banchan.util.toPriceFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor(
    private val cartAddUseCase: CartAddUseCase
) : ViewModel() {
    private val _currentDish = MutableStateFlow<SelectedDish?>(null)
    val currentDish: StateFlow<SelectedDish?> get() = _currentDish

    private val _amount = MutableStateFlow(1)
    val amount: StateFlow<Int> get() = _amount

    private val _totalPriceText = MutableStateFlow("0원")
    val totalPriceText: StateFlow<String> get() = _totalPriceText

    private val _cartAddEvent = MutableSharedFlow<UiEvent<Unit>>()
    val cartAddEvent: SharedFlow<UiEvent<Unit>> get() = _cartAddEvent

    fun setCurrentDish(dish: SelectedDish) {
        _currentDish.value = dish
        _totalPriceText.value = dish.sPriceText
    }

    fun updateAmount(isPlus: Boolean) {
        var currentAmount = amount.value
        if (isPlus) {
            _amount.value = ++currentAmount
        } else {
            if (currentAmount != 1) {
                _amount.value = --currentAmount
            }
        }
        val salePrice = currentDish.value?.sPrice ?: return
        _totalPriceText.value = "${(salePrice * currentAmount).toPriceFormat()}원"
    }

    fun addToCart() {
        val dish = currentDish.value ?: return
        val currentAmount = amount.value ?: return
        viewModelScope.launch {
            cartAddUseCase(dish.hash, currentAmount, dish.title)
                .onSuccess { _cartAddEvent.emit(UiEvent.Success(it)) }
                .onFailure {
                    _cartAddEvent.emit(UiEvent.Error(it as ErrorEntity))
                }
        }
    }
}