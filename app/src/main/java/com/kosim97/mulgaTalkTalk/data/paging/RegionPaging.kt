package com.kosim97.mulgaTalkTalk.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kosim97.mulgaTalkTalk.data.api.ApiResult
import com.kosim97.mulgaTalkTalk.data.remote.model.ResultData
import com.kosim97.mulgaTalkTalk.data.repository.region.RegionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

class RegionPaging(
    private val repository: RegionRepository,
    private val region: String,
    private val date: String,
    private val empty: MutableStateFlow<Boolean>
) : PagingSource<Int, ResultData>() {
    override fun getRefreshKey(state: PagingState<Int, ResultData>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultData> {
        return try {
            val page = params.key ?: 1
            var nextKey: Int? = if (page == 0) 20 else page + 20
            var data: ArrayList<ResultData>? = null

            val response = nextKey?.let { repository.getRegionDetail(page, it, region) }
            response
                ?.flowOn(Dispatchers.IO)
                ?.collect {
                when (it) {
                    is ApiResult.Success -> {
                        data = it.data?.list?.row
                        Log.d("test", "region $data")
                    }
                    else -> {
                        Log.d("test", "region paging fail")
                    }
                }
            }
            if (data == null) {
                if (page == 1) {
                    empty.emit(true)
                }
                nextKey = null
            }

            val responseData = mutableListOf<ResultData>()

            data?.filter {
                it.updateMonth == date
            }?.map {
                responseData.addAll(listOf(it))
            }

            data?.filterNot {
                it.updateMonth == date
            }?.map {
                nextKey = null
            }

            LoadResult.Page(
                data = responseData,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: HttpException) {
            Log.d("test", "region paging fail")
            return LoadResult.Error(e)
        } catch (e: Exception) {
            Log.d("test", "region paging fail")
            return LoadResult.Error(e)
        }
    }
}