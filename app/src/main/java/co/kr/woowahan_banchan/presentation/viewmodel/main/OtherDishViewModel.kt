package co.kr.woowahan_banchan.presentation.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.woowahan_banchan.domain.entity.dish.Dish
import co.kr.woowahan_banchan.domain.repository.Source
import co.kr.woowahan_banchan.domain.usecase.GetDishesUseCase
import co.kr.woowahan_banchan.presentation.viewmodel.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtherDishViewModel @Inject constructor(
    private val getDishesUseCase: GetDishesUseCase
) : ViewModel() {
    private val _otherDishes = MutableStateFlow<UiState<List<Dish>>>(UiState.Init)
    val otherDishes: StateFlow<UiState<List<Dish>>> get() = _otherDishes

    private var defaultMainDishes = listOf<Dish>()

    private val _sortedDishes = MutableLiveData<List<Dish>>(listOf())
    val sortedDishes: LiveData<List<Dish>> get() = _sortedDishes

    private val _dishAmount = MutableLiveData(0)
    val dishAmount: LiveData<Int> get() = _dishAmount

    private val _titleText = MutableLiveData("")
    val titleText: LiveData<String> get() = _titleText

    fun getDishes(dishType: String) {
        val source = if (dishType == DishType.SOUP.name) Source.SOUP else Source.SIDE
        viewModelScope.launch {
            getDishesUseCase(source)
                .catch { _otherDishes.value = UiState.Error("상품을 불러오는 것에 실패하였습니다.") }
                .collect {
                    _otherDishes.value = UiState.Success(it)
                }
        }
    }

    fun setSortedDishes(sortType: Int) {
        when (sortType) {
            0 -> _sortedDishes.value = defaultMainDishes
            1 -> _sortedDishes.value = defaultMainDishes.sortedByDescending { it.sPrice }
            2 -> _sortedDishes.value = defaultMainDishes.sortedBy { it.sPrice }
            3 -> _sortedDishes.value = defaultMainDishes.sortedWith { o1, o2 ->
                if (o1.discount == o2.discount) {
                    o1.sPrice - o2.sPrice
                } else {
                    o2.discount - o1.discount
                }
            }
        }
    }

    fun setDefaultMainDishes(list: List<Dish>) {
        defaultMainDishes = list
        _dishAmount.postValue(list.size)
    }

    fun setTitle(title: String) {
        _titleText.value = title
    }
}

enum class DishType {
    MAIN, SOUP, SIDE
}