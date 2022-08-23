package co.kr.woowahan_banchan.presentation.viewmodel.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.woowahan_banchan.domain.entity.history.HistoryItem
import co.kr.woowahan_banchan.domain.usecase.RecentlyViewedUseCase
import co.kr.woowahan_banchan.presentation.viewmodel.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentlyViewedViewModel @Inject constructor(
    private val recentlyViewedUseCase: RecentlyViewedUseCase
) : ViewModel() {
    private val _historyItems = MutableStateFlow<UiState<List<HistoryItem>>>(UiState.Init)
    val historyItems: StateFlow<UiState<List<HistoryItem>>> get() = _historyItems

    fun fetchHistoryItems() {
        viewModelScope.launch {
            recentlyViewedUseCase.invoke(previewMode = false)
                .catch { _historyItems.value = UiState.Error(it.message) }
                .collectLatest { result ->
                    result.onSuccess { _historyItems.value = UiState.Success(it) }
                }
        }
    }
}