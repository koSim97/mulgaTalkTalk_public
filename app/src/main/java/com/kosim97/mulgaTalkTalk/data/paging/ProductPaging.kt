package com.kosim97.mulgaTalkTalk.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kosim97.mulgaTalkTalk.data.api.ApiResult
import com.kosim97.mulgaTalkTalk.data.remote.model.ResultData
import com.kosim97.mulgaTalkTalk.data.repository.product.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

class ProductPaging(
    private val repository: ProductRepository,
    private val region: String,
    private val product: String,
    private val date: String,
    private val empty: MutableStateFlow<Boolean>
) : PagingSource<Int, ResultData>() {
    override fun getRefreshKey(state: PagingState<Int, ResultData>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultData> {
        return try {
            val page = params.key ?: 1
            var nextKey: Int? = if (page == 0) 10 else page + 10
            var data: ArrayList<ResultData>? = null

            val response =
                nextKey?.let { repository.getProductDetail(page, it, region, product, date) }
            response
                ?.flowOn(Dispatchers.IO)
                ?.collect {
                when (it) {
                    is ApiResult.Success -> {
                        data = it.data?.list?.row
                        Log.d("test","$data")
                    }
                    else -> {
                        Log.d("test","product paging fail")
                    }
                }
            }

            val responseData = mutableListOf<ResultData>()
            if (data != null) {
                responseData.addAll(data!!)
                empty.emit(false)
            } else {
                if (page == 1) {
                    empty.emit(true)
                }
                nextKey = null
            }

            LoadResult.Page(
                data = responseData,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: HttpException) {
            Log.d("test","product paging fail")
            return LoadResult.Error(e)
        } catch (e: Exception) {
            Log.d("test","product paging fail")
            return LoadResult.Error(e)
        }
    }
}