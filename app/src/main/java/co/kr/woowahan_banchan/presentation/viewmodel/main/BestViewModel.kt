package co.kr.woowahan_banchan.presentation.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.woowahan_banchan.domain.entity.dish.BestItem
import co.kr.woowahan_banchan.domain.usecase.GetBestsUseCase
import co.kr.woowahan_banchan.presentation.viewmodel.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BestViewModel @Inject constructor(
    private val getBestsUseCase: GetBestsUseCase
) : ViewModel() {
    private val _bestItems = MutableStateFlow<UiState<List<BestItem>>>(UiState.Init)
    val bestItems: StateFlow<UiState<List<BestItem>>> get() = _bestItems

    fun getBests() {
        viewModelScope.launch {
            getBestsUseCase()
                .catch { _bestItems.value = UiState.Error("상품을 불러오는 것에 실패하였습니다.") }
                .collect {
                    _bestItems.value = UiState.Success(it)
                }
        }
    }
}