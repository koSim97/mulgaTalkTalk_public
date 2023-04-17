package com.kosim97.mulgaTalkTalk.data.repository.region

import com.kosim97.mulgaTalkTalk.data.api.ApiResult
import com.kosim97.mulgaTalkTalk.data.remote.model.ApiData
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RegionRemoteDataSource {
    suspend fun getSampleRegion(start: Int, end: Int, region: String): Response<ApiData>
}