package co.kr.woowahan_banchan.domain.entity.error

/*
* ErrorEntity Type
* RetryableError - Network 통신과 관련하여 단순히 재시도해도 성공할 가능성이 있는 Error
* ConditionalError - 특정 조건이 충족되지 않아 발생하는 Error. 즉, 해당 조건을 충족하면 해결될 Error
* UnknownError - 위 2가지와 관련되지 않은 Error
*/
sealed class ErrorEntity: Throwable() {
    object RetryableError: ErrorEntity()
    object ConditionalError: ErrorEntity()
    object UnknownError: ErrorEntity()
}
