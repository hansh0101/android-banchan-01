package co.kr.woowahan_banchan.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.woowahan_banchan.domain.usecase.GetCartItemCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCartItemCountUseCase: GetCartItemCountUseCase
) : ViewModel() {

    private val _cartCount = MutableLiveData(0)
    val cartCount: LiveData<Int> get() = _cartCount

    fun getCartItemCount() = viewModelScope.launch {
        getCartItemCountUseCase()
            .catch { _cartCount.value = 0 }
            .collect {
                _cartCount.value = it
            }
    }
}