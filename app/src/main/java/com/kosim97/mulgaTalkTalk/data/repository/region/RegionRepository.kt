package com.kosim97.mulgaTalkTalk.data.repository.region

import com.kosim97.mulgaTalkTalk.data.api.ApiResult
import com.kosim97.mulgaTalkTalk.data.api.BaseFlowResponse
import com.kosim97.mulgaTalkTalk.data.remote.model.ApiData
import com.kosim97.mulgaTalkTalk.data.remote.region.RegionService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegionRepository @Inject constructor(
    private val remote: RegionRemoteDataSource
):BaseFlowResponse() {

    fun getSampleRegion(start: Int, end: Int, region: String) : Flow<ApiResult<ApiData>> = flow {
        emit(flowCall(remote.getSampleRegion(start, end, region)))
    }
}