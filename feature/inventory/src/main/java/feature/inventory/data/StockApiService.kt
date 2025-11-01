package feature.inventory.data

import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

@Serializable
data class StockRequest(
    val pageNum: Int,
    val pageSize: Int
)

@Serializable
data class StockResponse(
    val errorDetails: String? = null,
    val result: StockResult,
    val success: Boolean,
    val traceId: String
)

@Serializable
data class StockResult(
    val message: String,
    val pageNum: Int,
    val pageSize: Int,
    val stocks: List<StockItem>,
    val total: Int
)

@Serializable
data class StockItem(
    val name: String,
    val price: Long
)

interface StockApiService {
    @Headers("Content-Type: application/json")
    @POST("webhook/bp/query")
    suspend fun getStocks(@Body request: StockRequest): Response<StockResponse>
}