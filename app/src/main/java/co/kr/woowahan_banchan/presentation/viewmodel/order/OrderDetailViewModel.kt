package co.kr.woowahan_banchan.presentation.viewmodel.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderItem
import co.kr.woowahan_banchan.domain.usecase.OrderDetailUseCase
import co.kr.woowahan_banchan.domain.usecase.OrderTimeUseCase
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
    private val _orderTime = MutableSharedFlow<UiState<Long>>()
    val orderTime: SharedFlow<UiState<Long>> get() = _orderTime

    fun fetchOrderItems(orderId: Long) {
        viewModelScope.launch {
            orderDetailUseCase(orderId)
                .onSuccess { _orderItems.value = UiState.Success(it) }
                .onFailure { _orderItems.value = UiState.Error(it.message) }
        }
        viewModelScope.launch {
            orderTimeUseCase(orderId)
                .onSuccess {
                    if (it != 0L) _orderTime.emit(UiState.Success(it))
                    else _orderTime.emit(UiState.Error("주문 시간을 조회하는데 실패했습니다"))
                }
                .onFailure {
                    _orderTime.emit(UiState.Error(it.message))
                }
        }
    }
}