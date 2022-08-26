package co.kr.woowahan_banchan.presentation.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.woowahan_banchan.domain.entity.dish.Dish
import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity
import co.kr.woowahan_banchan.domain.repository.Source
import co.kr.woowahan_banchan.domain.usecase.GetDishesUseCase
import co.kr.woowahan_banchan.presentation.viewmodel.UiEvent
import co.kr.woowahan_banchan.presentation.viewmodel.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainDishViewModel @Inject constructor(
    private val getDishesUseCase: GetDishesUseCase
) : ViewModel() {
    private val _mainDishes = MutableStateFlow<UiState<List<Dish>>>(UiState.Init)
    val mainDishes: StateFlow<UiState<List<Dish>>> get() = _mainDishes
    private val _isGridMode = MutableStateFlow<Boolean>(true)
    val isGridMode: StateFlow<Boolean> get() = _isGridMode
    private val _sortedDishes = MutableStateFlow<List<Dish>>(listOf())
    val sortedDishes: StateFlow<List<Dish>> get() = _sortedDishes

    private val _mainDishesEvent = MutableSharedFlow<UiEvent<Unit>>()
    val mainDishesEvent: SharedFlow<UiEvent<Unit>> get() = _mainDishesEvent

    private var defaultMainDishes = listOf<Dish>()
    private var collectJob: Job? = null

    init {
        getDishes()
    }

    fun getDishes() {
        if (collectJob == null) {
            collectJob = viewModelScope.launch {
                getDishesUseCase(Source.MAIN)
                    .collect { result ->
                        result.onSuccess {
                            defaultMainDishes = it
                            _mainDishes.value = UiState.Success(it)
                        }.onFailure {
                            _mainDishes.value = UiState.Error(it.message)
                            _mainDishesEvent.emit(UiEvent.Error(it as ErrorEntity))
                        }
                    }
            }
        }
    }

    fun cancelCollectJob() {
        collectJob?.cancel()
        collectJob = null
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

    fun setGridMode(isGrid: Boolean) {
        _isGridMode.value = isGrid
    }

    fun reFetchDishes() {
        cancelCollectJob()
        getDishes()
    }
}