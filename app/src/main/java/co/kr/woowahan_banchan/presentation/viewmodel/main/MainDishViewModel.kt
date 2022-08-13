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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainDishViewModel @Inject constructor(
    private val getDishesUseCase: GetDishesUseCase
) : ViewModel() {
    private val _mainDishes = MutableStateFlow<UiState<List<Dish>>>(UiState.Init)
    val mainDishes: StateFlow<UiState<List<Dish>>> get() = _mainDishes

    private val _isGridMode = MutableLiveData(true)
    val isGridMode : LiveData<Boolean> get() = _isGridMode

    private var defaultMainDishes = listOf<Dish>()

    private val _sortedDishes = MutableLiveData<List<Dish>>(listOf())
    val sortedDishes :LiveData<List<Dish>> get() = _sortedDishes

    fun getDishes(source: Source) = viewModelScope.launch {
        getDishesUseCase(source).collect {
            _mainDishes.value = UiState.Success(it)
        }
    }

    fun setSortedDishes(sortType : Int) {
        when(sortType){
            0 -> _sortedDishes.value = defaultMainDishes
            1 -> _sortedDishes.value = defaultMainDishes.sortedByDescending { it.nPrice }
            2 -> _sortedDishes.value = defaultMainDishes.sortedBy { it.nPrice }
            3 -> _sortedDishes.value = defaultMainDishes.sortedByDescending { it.discount }
        }
    }

    fun setDefaultMainDishes(list : List<Dish>){
        defaultMainDishes = list
    }

    fun setGridMode(isGrid : Boolean){
        _isGridMode.value = isGrid
    }
}