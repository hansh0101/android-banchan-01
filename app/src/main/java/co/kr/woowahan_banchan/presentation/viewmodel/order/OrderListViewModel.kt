package co.kr.woowahan_banchan.presentation.viewmodel.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity
import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderHistory
import co.kr.woowahan_banchan.domain.usecase.OrderListUseCase
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
class OrderListViewModel @Inject constructor(
    private val orderListUseCase: OrderListUseCase
) : ViewModel() {
    private val _orderHistories = MutableStateFlow<UiState<List<OrderHistory>>>(UiState.Init)
    val orderHistories: StateFlow<UiState<List<OrderHistory>>> get() = _orderHistories
    private val _orderHistoriesEvents = MutableSharedFlow<UiEvent<Unit>>()
    val orderHistoriesEvents: SharedFlow<UiEvent<Unit>> get() = _orderHistoriesEvents

    fun fetchOrderHistories() {
        viewModelScope.launch {
            orderListUseCase()
                .onSuccess { _orderHistories.value = UiState.Success(it) }
                .onFailure {
                    _orderHistories.value = UiState.Error(it.message)
                    _orderHistoriesEvents.emit(UiEvent.Error(it as ErrorEntity))
                }
        }
    }
}