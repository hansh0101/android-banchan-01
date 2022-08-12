package co.kr.woowahan_banchan.presentation.viewmodel

sealed class UiState<out T> {
    object Init : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String?) : UiState<Nothing>()
}