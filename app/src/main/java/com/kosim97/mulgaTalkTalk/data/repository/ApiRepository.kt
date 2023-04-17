package com.kosim97.mulgaTalkTalk.data.repository

import android.util.Log
import com.kosim97.mulgaTalkTalk.data.api.ApiResult
import com.kosim97.mulgaTalkTalk.data.api.ApiService
import com.kosim97.mulgaTalkTalk.data.api.BaseFlowResponse
import com.kosim97.mulgaTalkTalk.data.HomeDetail
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

class ApiRepository @Inject constructor(
    private val apiService: ApiService
) : BaseFlowResponse() {

    suspend fun getRegionDetail(
        start: Int, end: Int, region: String
    ): Flow<ApiResult<HomeDetail>> = flow {
        emit(flowCall(apiService.getRegionApi(start, end, region)))
    }

    suspend fun getProductDetail(
        start: Int, end: Int, region: String, product: String, date: String
    ): Flow<ApiResult<HomeDetail>> = flow {
        emit(flowCall(apiService.getSearchApi(start, end, region, product, date)))
    }

    suspend fun getAllDetail(
        start: Int,
        end: Int
    ): Flow<ApiResult<HomeDetail>> = flow {
        emit(flowCall(apiService.getAllApi(start, end)))
    }
}