package com.kosim97.mulgaTalkTalk.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kosim97.mulgaTalkTalk.data.Detail
import com.kosim97.mulgaTalkTalk.data.api.ApiResult
import com.kosim97.mulgaTalkTalk.data.repository.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.HttpException

class ProductPaging(
    private val repository: ApiRepository,
    private val region: String,
    private val product: String,
    private val date: String,
    private val empty: MutableStateFlow<Boolean>
) : PagingSource<Int, Detail>() {
    override fun getRefreshKey(state: PagingState<Int, Detail>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Detail> {
        return try {
            val page = params.key ?: 1
            var nextKey: Int? = if (page == 0) 10 else page + 10
            var data: ArrayList<Detail>? = null

            val response =
                nextKey?.let { repository.getProductDetail(page, it, region, product, date) }
            response?.collect {
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

            val responseData = mutableListOf<Detail>()
            if (data != null) {
                responseData.addAll(data!!)
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