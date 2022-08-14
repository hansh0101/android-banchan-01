package co.kr.woowahan_banchan.presentation.viewmodel.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.kr.woowahan_banchan.domain.entity.dish.Dish
import co.kr.woowahan_banchan.util.toPriceFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor() : ViewModel(){
    private val _currentDish = MutableLiveData<Dish>()
    val currentDish : LiveData<Dish> get() = _currentDish

    private val _amount = MutableLiveData(1)
    val amount : LiveData<Int> get() = _amount

    private val _totalPriceText = MutableLiveData("0원")
    val totalPriceText : LiveData<String> get() = _totalPriceText


    fun setCurrentDish(dish : Dish) {
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
}