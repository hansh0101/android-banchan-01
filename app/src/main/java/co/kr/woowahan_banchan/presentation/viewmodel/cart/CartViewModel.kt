package co.kr.woowahan_banchan.presentation.viewmodel.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import co.kr.woowahan_banchan.domain.entity.cart.CartItem
import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity
import co.kr.woowahan_banchan.domain.entity.history.HistoryItem
import co.kr.woowahan_banchan.domain.usecase.GetCartItemsUseCase
import co.kr.woowahan_banchan.domain.usecase.HistoryUseCase
import co.kr.woowahan_banchan.domain.usecase.OrderUseCase
import co.kr.woowahan_banchan.presentation.viewmodel.UiEvents
import co.kr.woowahan_banchan.presentation.viewmodel.UiStates
import co.kr.woowahan_banchan.presentation.worker.CartUpdateWorker
import co.kr.woowahan_banchan.util.listToString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val historyUseCase: HistoryUseCase,
    private val orderUseCase: OrderUseCase
) : ViewModel() {
    private val _cartItems = MutableStateFlow<UiStates<List<CartItem>>>(UiStates.Init)
    val cartItems: StateFlow<UiStates<List<CartItem>>> get() = _cartItems
    private val _historyItems = MutableStateFlow<UiStates<List<HistoryItem>>>(UiStates.Init)
    val historyItems: StateFlow<UiStates<List<HistoryItem>>> get() = _historyItems
    private val _isSelectedAll = MutableStateFlow(true)
    val isSelectedAll: StateFlow<Boolean> get() = _isSelectedAll
    private val _orderId = MutableStateFlow<UiStates<Long>>(UiStates.Init)
    val orderId: StateFlow<UiStates<Long>> get() = _orderId

    private val _cartEvent = MutableSharedFlow<UiEvents<Unit>>()
    val cartEvent: SharedFlow<UiEvents<Unit>> get() = _cartEvent
    private val _orderEvent = MutableSharedFlow<UiEvents<Unit>>()
    val orderEvent: SharedFlow<UiEvents<Unit>> get() = _orderEvent

    private val originalCart = mutableListOf<CartItem>()
    var isOrdered = false
    private var collectJob: Job? = null

    init {
        getCartItems()
        getRecentlyViewed()
    }

    fun getCartItems() {
        collectJob = viewModelScope.launch {
            getCartItemsUseCase()
                .collect { result ->
                    result.onSuccess {
                        _cartItems.value = UiStates.Success(it)
                        it.forEach { item ->
                            originalCart.add(item.copy())
                        }
                    }.onFailure {
                        _cartItems.value = UiStates.Error(it.message)
                        _cartEvent.emit(UiEvents.Error(it as ErrorEntity))
                    }
                }
        }
    }

    fun reFetchCartItems() {
        collectJob?.cancel()
        getCartItems()
    }

    fun getRecentlyViewed() {
        viewModelScope.launch {
            historyUseCase(true)
                .collect { result ->
                    result.onSuccess { _historyItems.value = UiStates.Success(it) }
                        .onFailure { _historyItems.value = UiStates.Error("최근 조회 메뉴를 불러오지 못했어요!") }
                }
        }
    }

    fun setSelectedAll(isSelectedAll: Boolean) {
        _isSelectedAll.value = isSelectedAll
    }

    fun setSelectedAll(cartList: List<CartItem>) {
        cartList.forEach {
            if (!it.isSelected) {
                _isSelectedAll.value = false
                return
            }
        }
        _isSelectedAll.value = true
    }

    fun orderStart(cartList: List<CartItem>) {
        viewModelScope.launch {
            orderUseCase(originalCart, cartList).onSuccess {
                isOrdered = true
                _orderId.value = UiStates.Success(it)
            }.onFailure {
                isOrdered = false
                _orderId.value = UiStates.Error(it.message)
                _orderEvent.emit(UiEvents.Error(it as ErrorEntity))
            }
        }
    }

    fun updateCartItems(cartList: List<CartItem>, workManager: WorkManager) {
        val inputData = Data.Builder()
            .putString("original_list", listToString(originalCart))
            .putString("update_list", listToString(cartList))
            .build()
        val workRequest = OneTimeWorkRequestBuilder<CartUpdateWorker>()
            .setInputData(inputData)
            .build()
        workManager.enqueue(workRequest)
    }
}