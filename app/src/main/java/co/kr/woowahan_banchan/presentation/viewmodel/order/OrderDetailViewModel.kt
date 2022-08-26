package co.kr.woowahan_banchan.presentation.viewmodel.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity
import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderItem
import co.kr.woowahan_banchan.domain.usecase.OrderDetailUseCase
import co.kr.woowahan_banchan.domain.usecase.OrderTimeUseCase
import co.kr.woowahan_banchan.presentation.viewmodel.UiEvent
import co.kr.woowahan_banchan.presentation.viewmodel.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val orderDetailUseCase: OrderDetailUseCase,
    private val orderTimeUseCase: OrderTimeUseCase
) : ViewModel() {
    private val _orderItems = MutableStateFlow<UiState<List<OrderItem>>>(UiState.Init)
    val orderItems: StateFlow<UiState<List<OrderItem>>> get() = _orderItems
    private val _orderTime = MutableStateFlow<UiState<Long>>(UiState.Init)
    val orderTime: StateFlow<UiState<Long>> get() = _orderTime
    var time: Long = 0L

    private val _orderItemsEvent = MutableSharedFlow<UiEvent<Unit>>()
    val orderItemsEvent: SharedFlow<UiEvent<Unit>> get() = _orderItemsEvent
    private val _orderTimeEvent = MutableSharedFlow<UiEvent<Unit>>()
    val orderTimeEvent: SharedFlow<UiEvent<Unit>> get() = _orderTimeEvent

    fun fetchOrderItems(orderId: Long) {
        viewModelScope.launch {
            orderDetailUseCase(orderId)
                .onSuccess { _orderItems.value = UiState.Success(it) }
                .onFailure {
                    _orderItems.value = UiState.Error(it.message)
                    _orderItemsEvent.emit(UiEvent.Error(it as ErrorEntity))
                }
        }
        viewModelScope.launch {
            orderTimeUseCase(orderId)
                .onSuccess {
                    if (it != 0L) {
                        _orderTime.value = UiState.Success(it)
                        time = it
                    } else {
                        _orderTime.value = UiState.Error("주문 시간을 조회하는데 실패했습니다")
                    }
                }.onFailure {
                    _orderTime.value = UiState.Error(it.message)
                    _orderItemsEvent.emit(UiEvent.Error(it as ErrorEntity))
                }
        }
    }
}