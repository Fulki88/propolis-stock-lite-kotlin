package feature.inventory.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepository @Inject constructor(
    private val apiService: StockApiService
) {
    suspend fun getStocks(pageNum: Int, pageSize: Int = 30): Result<StockResult> {
        return try {
            val response = apiService.getStocks(StockRequest(pageNum, pageSize))
            if (response.isSuccessful) {
                val body = response.body()
                if (!body.isNullOrEmpty() && body.first().success) {
                    Result.success(body.first().result)
                } else {
                    Result.failure(Exception(body?.firstOrNull()?.errorDetails ?: "Unknown error"))
                }
            } else {
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getStocksFlow(pageSize: Int = 30): Flow<PagingResult<StockItem>> = flow {
        var currentPage = 1
        val allItems = mutableListOf<StockItem>()
        var hasMorePages = true
        
        emit(PagingResult.Loading)
        
        while (hasMorePages) {
            try {
                val result = getStocks(currentPage, pageSize)
                result.fold(
                    onSuccess = { stockResult ->
                        allItems.addAll(stockResult.stocks)
                        hasMorePages = stockResult.stocks.size == pageSize
                        emit(PagingResult.Success(
                            items = allItems.toList(),
                            hasMore = hasMorePages,
                            total = stockResult.total
                        ))
                        currentPage++
                    },
                    onFailure = { error ->
                        emit(PagingResult.Error(error.message ?: "Unknown error"))
                        hasMorePages = false
                    }
                )
            } catch (e: Exception) {
                emit(PagingResult.Error(e.message ?: "Unknown error"))
                hasMorePages = false
            }
        }
    }
}

sealed class PagingResult<out T> {
    object Loading : PagingResult<Nothing>()
    data class Success<T>(
        val items: List<T>,
        val hasMore: Boolean,
        val total: Int = 0
    ) : PagingResult<T>()
    data class Error(val message: String) : PagingResult<Nothing>()
}