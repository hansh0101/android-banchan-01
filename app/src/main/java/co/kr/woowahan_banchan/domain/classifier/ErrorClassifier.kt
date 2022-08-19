package co.kr.woowahan_banchan.domain.classifier

import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity

interface ErrorClassifier {
    fun classifyError(throwable: Throwable): ErrorEntity
}