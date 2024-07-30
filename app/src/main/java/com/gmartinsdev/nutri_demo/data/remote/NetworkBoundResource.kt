import com.gmartinsdev.nutri_demo.data.remote.ApiError
import com.gmartinsdev.nutri_demo.data.remote.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

/**
 * A generic class that can provide a resource backed by both the database and the network
 */
inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline onFetchFailed: (Throwable) -> Unit = { },
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    val data = query().first()

    val flow = if (shouldFetch(data)) {

        try {
            saveFetchResult(fetch())
            val queryResult = query().first()
            ApiResult.success(queryResult)
        } catch (throwable: Throwable) {
            onFetchFailed(throwable)
            ApiResult.error(ApiError(0, throwable.message ?: ""))
        }
    } else {
        ApiResult.success(data)
    }

    emit(flow)
}