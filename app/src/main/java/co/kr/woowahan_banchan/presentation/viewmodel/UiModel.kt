package co.kr.woowahan_banchan.presentation.viewmodel

import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity

sealed class UiStates<out T> {
    object Init : UiStates<Nothing>()
    data class Success<T>(val data: T) : UiStates<T>()
    data class Error(val message: String?) : UiStates<Nothing>()
}

sealed class UiEvents<out T> {
    data class Success<T>(val data: T) : UiEvents<T>()
    data class Error(val error: ErrorEntity) : UiEvents<Nothing>()
}