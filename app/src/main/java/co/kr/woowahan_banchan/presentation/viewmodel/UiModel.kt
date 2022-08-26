package co.kr.woowahan_banchan.presentation.viewmodel

import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity

sealed class UiState<out T> {
    object Init : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String?) : UiState<Nothing>()
}

sealed class UiEvent<out T> {
    data class Success<T>(val data: T) : UiEvent<T>()
    data class Error(val error: ErrorEntity) : UiEvent<Nothing>()
}