package co.kr.woowahan_banchan.domain.entity.result

import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity

sealed class CustomResult<T> {
    data class Success<T>(val data: T): CustomResult<T>()
    data class Error<T>(val error: ErrorEntity): CustomResult<T>()
}
