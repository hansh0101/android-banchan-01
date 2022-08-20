package co.kr.woowahan_banchan.presentation.viewmodel.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.woowahan_banchan.domain.entity.cart.CartItem
import co.kr.woowahan_banchan.domain.entity.history.HistoryItem
import co.kr.woowahan_banchan.domain.usecase.GetCartItemsUseCase
import co.kr.woowahan_banchan.domain.usecase.OrderUseCase
import co.kr.woowahan_banchan.domain.usecase.RecentlyViewedUseCase
import co.kr.woowahan_banchan.domain.usecase.UpdateCartItemsUseCase
import co.kr.woowahan_banchan.presentation.viewmodel.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val recentlyViewedUseCase: RecentlyViewedUseCase,
    private val orderUseCase: OrderUseCase,
    private val updateCartItemsUseCase: UpdateCartItemsUseCase
) : ViewModel() {

    private val _cartItems = MutableStateFlow<UiState<List<CartItem>>>(UiState.Init)
    val cartItems: StateFlow<UiState<List<CartItem>>> get() = _cartItems

    private val _historyItems = MutableStateFlow<UiState<List<HistoryItem>>>(UiState.Init)
    val historyItems: StateFlow<UiState<List<HistoryItem>>> get() = _historyItems

    private val _isSelectedAll = MutableStateFlow(true)
    val isSelectedAll: StateFlow<Boolean> get() = _isSelectedAll

    private val _orderId = MutableStateFlow<UiState<Long>>(UiState.Init)
    val orderId: StateFlow<UiState<Long>> get() = _orderId

    private val originalCart = mutableListOf<CartItem>()
    var isOrdered = false

    fun getCartItems() {
        viewModelScope.launch {
            getCartItemsUseCase()
                .catch { _cartItems.value = UiState.Error("장바구니 내역을 불러오지 못함") }
                .collect {
                    _cartItems.value = UiState.Success(it)
                    it.forEach { item ->
                        originalCart.add(item.copy())
                    }
                }
        }
    }

    fun getRecentlyViewed() {
        viewModelScope.launch {
            recentlyViewedUseCase(true)
                .catch { _historyItems.value = UiState.Error("최근 방문 내역을 불러오지 못함") }
                .collect {
                    _historyItems.value = UiState.Success(it)
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
                _orderId.value = UiState.Success(it)
            }.onFailure {
                isOrdered = false
                _orderId.value = UiState.Error("주문을 실패했습니다.")
            }
        }
    }

    fun updateCartItems(cartList: List<CartItem>) {
        CoroutineScope(Dispatchers.Main).launch {
            updateCartItemsUseCase(originalCart, cartList)
            this.cancel()
        }
    }
}