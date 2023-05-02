package com.kosim97.mulgaTalkTalk.data.repository.all

import com.kosim97.mulgaTalkTalk.data.api.ApiResult
import com.kosim97.mulgaTalkTalk.data.api.BaseFlowResponse
import com.kosim97.mulgaTalkTalk.data.remote.model.ApiData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AllRepository @Inject constructor(
    private val remote: AllRemoteDataSource
): BaseFlowResponse() {
    fun getAllData(start: Int, end: Int): Flow<ApiResult<ApiData>> = flow {
        emit(flowCall(remote.getAllData(start, end)))
    }
}