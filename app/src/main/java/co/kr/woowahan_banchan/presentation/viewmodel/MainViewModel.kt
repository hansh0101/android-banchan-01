package co.kr.woowahan_banchan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.woowahan_banchan.domain.usecase.GetCartItemCountUseCase
import co.kr.woowahan_banchan.domain.usecase.LatestOrderTimeUseCase
import co.kr.woowahan_banchan.util.calculateDiffToMinute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCartItemCountUseCase: GetCartItemCountUseCase,
    private val latestOrderTimeUseCase: LatestOrderTimeUseCase
) : ViewModel() {

    private val _cartCount = MutableStateFlow(0)
    val cartCount: StateFlow<Int> get() = _cartCount

    private val _isOrderCompleted = MutableStateFlow<Boolean>(true)
    val isOrderCompleted: StateFlow<Boolean> get() = _isOrderCompleted

    fun getCartItemCount() = viewModelScope.launch {
        getCartItemCountUseCase()
            .catch { _cartCount.value = 0 }
            .collect {
                _cartCount.value = it
            }
    }

    fun fetchLatestOrderTime() {
        viewModelScope.launch {
            latestOrderTimeUseCase().collect { time ->
                _isOrderCompleted.value = Date().time.calculateDiffToMinute(time) >= 20
            }
        }
    }
}