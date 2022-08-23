package co.kr.woowahan_banchan.data.repository

import co.kr.woowahan_banchan.data.datasource.local.cart.CartDataSource
import co.kr.woowahan_banchan.data.datasource.local.history.HistoryDataSource
import co.kr.woowahan_banchan.data.datasource.remote.detail.DetailDataSource
import co.kr.woowahan_banchan.domain.entity.history.HistoryItem
import co.kr.woowahan_banchan.domain.repository.HistoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyDataSource: HistoryDataSource,
    private val cartDataSource: CartDataSource,
    private val detailDataSource: DetailDataSource,
    private val coroutineDispatcher: CoroutineDispatcher
) : HistoryRepository {
    override suspend fun addToHistory(hash: String, name: String): Result<Unit> {
        return historyDataSource.insertItem(hash, name)
    }

    override fun getHistories(previewMode: Boolean): Flow<Result<List<HistoryItem>>> {
        return combine(
            if (previewMode) historyDataSource.getPreviewItems() else historyDataSource.getItems(),
            cartDataSource.getItems()
        ) { historyDtoList, cartDtoList ->
            Pair(historyDtoList, cartDtoList)
        }.map { pair ->
            val historyDtoList = pair.first
            val cartDtoList = pair.second
            Result.success(historyDtoList.mapNotNull { historyDto ->
                val apiResult = detailDataSource.getDetail(historyDto.hash)
                when (apiResult.isSuccess) {
                    true -> {
                        apiResult.getOrNull()?.data?.toHistoryItem(
                            historyDto.hash,
                            historyDto.name,
                            historyDto.time,
                            cartDtoList.find { it.hash == historyDto.hash } != null
                        )
                    }
                    false -> {
                        null
                    }
                }
            })
        }.catch {
            emit(Result.failure(it))
        }.flowOn(coroutineDispatcher)
    }
}