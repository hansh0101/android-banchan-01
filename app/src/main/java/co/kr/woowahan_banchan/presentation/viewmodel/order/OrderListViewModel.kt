package co.kr.woowahan_banchan.presentation.viewmodel.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity
import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderHistory
import co.kr.woowahan_banchan.domain.usecase.OrderListUseCase
import co.kr.woowahan_banchan.presentation.viewmodel.UiEvents
import co.kr.woowahan_banchan.presentation.viewmodel.UiStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val orderListUseCase: OrderListUseCase
) : ViewModel() {
    private val _orderHistories = MutableStateFlow<UiStates<List<OrderHistory>>>(UiStates.Init)
    val orderHistories: StateFlow<UiStates<List<OrderHistory>>> get() = _orderHistories
    private val _orderHistoriesEvents = MutableSharedFlow<UiEvents<Unit>>()
    val orderHistoriesEvents: SharedFlow<UiEvents<Unit>> get() = _orderHistoriesEvents

    fun fetchOrderHistories() {
        viewModelScope.launch {
            orderListUseCase()
                .onSuccess { _orderHistories.value = UiStates.Success(it) }
                .onFailure {
                    _orderHistories.value = UiStates.Error(it.message)
                    _orderHistoriesEvents.emit(UiEvents.Error(it as ErrorEntity))
                }
        }
    }
}