package co.kr.woowahan_banchan.data.extension

import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity
import retrofit2.HttpException
import java.io.IOException
import java.io.InterruptedIOException
import java.net.HttpRetryException
import java.net.HttpURLConnection
import java.net.UnknownHostException
import java.nio.channels.InterruptedByTimeoutException

inline fun <T, R> T.runCatchingErrorEntity(block: T.() -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: Throwable) {
        Result.failure(e.toErrorEntity())
    }
}

fun Throwable.toErrorEntity(): ErrorEntity {
    return when (this) {
        is ErrorEntity -> this
        is HttpRetryException, is InterruptedIOException, is InterruptedByTimeoutException -> ErrorEntity.RetryableError
        is UnknownHostException -> ErrorEntity.ConditionalError
        is IOException -> ErrorEntity.UnknownError
        is HttpException -> {
            when (this.code()) {
                HttpURLConnection.HTTP_CLIENT_TIMEOUT, HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> ErrorEntity.RetryableError
                else -> ErrorEntity.UnknownError
            }
        }
        else -> ErrorEntity.UnknownError
    }
}