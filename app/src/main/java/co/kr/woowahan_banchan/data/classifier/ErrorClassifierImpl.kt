package co.kr.woowahan_banchan.data.classifier

import co.kr.woowahan_banchan.domain.classifier.ErrorClassifier
import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity
import retrofit2.HttpException
import java.io.IOException
import java.io.InterruptedIOException
import java.net.HttpRetryException
import java.net.HttpURLConnection
import java.net.UnknownHostException
import java.nio.channels.InterruptedByTimeoutException

class ErrorClassifierImpl : ErrorClassifier {
    override fun classifyError(throwable: Throwable): ErrorEntity =
        when (throwable) {
            is HttpRetryException, is InterruptedIOException, is InterruptedByTimeoutException -> ErrorEntity.RetryableError
            is UnknownHostException -> ErrorEntity.ConditionalError
            is IOException -> ErrorEntity.UnknownError
            is HttpException -> {
                when (throwable.code()) {
                    HttpURLConnection.HTTP_CLIENT_TIMEOUT, HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> ErrorEntity.RetryableError
                    else -> ErrorEntity.UnknownError
                }
            }
            else -> ErrorEntity.UnknownError
        }
}