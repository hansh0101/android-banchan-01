package co.kr.woowahan_banchan.presentation.viewmodel.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.woowahan_banchan.domain.entity.dish.SelectedDish
import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity
import co.kr.woowahan_banchan.domain.usecase.CartAddUseCase
import co.kr.woowahan_banchan.presentation.viewmodel.UiEvents
import co.kr.woowahan_banchan.util.toPriceFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor(
    private val cartAddUseCase: CartAddUseCase
) : ViewModel() {
    private val _currentDish = MutableLiveData<SelectedDish>()
    val currentDish: LiveData<SelectedDish> get() = _currentDish

    private val _amount = MutableLiveData(1)
    val amount: LiveData<Int> get() = _amount

    private val _totalPriceText = MutableLiveData("0원")
    val totalPriceText: LiveData<String> get() = _totalPriceText

    private val _isAddSuccess = MutableSharedFlow<UiEvents<Unit>>()
    val isAddSuccess: SharedFlow<UiEvents<Unit>> get() = _isAddSuccess

    fun setCurrentDish(dish: SelectedDish) {
        _currentDish.value = dish
        _totalPriceText.value = dish.sPriceText
    }

    fun updateAmount(isPlus: Boolean) {
        var currentAmount = amount.value ?: return
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
                .onSuccess { _isAddSuccess.emit(UiEvents.Success(it)) }
                .onFailure {
                    _isAddSuccess.emit(UiEvents.Error(it as ErrorEntity))
                }
        }
    }
}