package co.kr.woowahan_banchan.presentation.viewmodel.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderHistory
import co.kr.woowahan_banchan.domain.usecase.OrderListUseCase
import co.kr.woowahan_banchan.presentation.viewmodel.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val orderListUseCase: OrderListUseCase
) : ViewModel() {
    private val _orderHistories = MutableStateFlow<UiState<List<OrderHistory>>>(UiState.Init)
    val orderHistories: StateFlow<UiState<List<OrderHistory>>> get() = _orderHistories

    fun fetchOrderHistories() {
        viewModelScope.launch {
            orderListUseCase()
                .onSuccess { _orderHistories.value = UiState.Success(it) }
                .onFailure { _orderHistories.value = UiState.Error(it.message) }
        }
    }
}