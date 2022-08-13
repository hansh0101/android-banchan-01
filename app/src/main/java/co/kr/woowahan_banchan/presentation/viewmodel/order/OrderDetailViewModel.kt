package co.kr.woowahan_banchan.presentation.viewmodel.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderItem
import co.kr.woowahan_banchan.domain.usecase.OrderDetailUseCase
import co.kr.woowahan_banchan.presentation.viewmodel.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val orderDetailUseCase: OrderDetailUseCase
) : ViewModel() {
    private val _orderItems = MutableStateFlow<UiState<List<OrderItem>>>(UiState.Init)
    val orderItems: StateFlow<UiState<List<OrderItem>>> get() = _orderItems

    fun fetchOrderItems(orderId: Long) {
        viewModelScope.launch {
            orderDetailUseCase(orderId)
                .onSuccess { _orderItems.value = UiState.Success(it) }
                .onFailure { _orderItems.value = UiState.Error(it.message) }
        }
    }
}