package co.kr.woowahan_banchan.presentation.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import co.kr.woowahan_banchan.domain.entity.cart.CartItem
import co.kr.woowahan_banchan.domain.usecase.UpdateCartItemsUseCase
import co.kr.woowahan_banchan.util.stringToList
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class CartUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val updateCartItemsUseCase: UpdateCartItemsUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val originalList = inputData.getString("original_list")
            ?.let { stringToList<CartItem>(it) }
            ?: return Result.failure()
        val updateList = inputData.getString("update_list")
            ?.let { stringToList<CartItem>(it) }
            ?: return Result.failure()

        updateCartItemsUseCase(originalList, updateList).onFailure {
            return Result.retry()
        }

        return Result.success()
    }
}