package co.kr.woowahan_banchan.presentation.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import co.kr.woowahan_banchan.domain.usecase.UpdateOrderUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class OrderUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val updateOrderUseCase: UpdateOrderUseCase
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val orderId = inputData.getLong(ORDER_ID, -1L)
        if (orderId != -1L) {
            updateOrderUseCase(orderId).onFailure {
                return Result.failure()
            }
        }
        return Result.success()
    }
    companion object {
        const val ORDER_ID = "order_id"
    }
}