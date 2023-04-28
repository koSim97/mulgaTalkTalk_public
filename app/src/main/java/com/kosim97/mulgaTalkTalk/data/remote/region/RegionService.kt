package com.kosim97.mulgaTalkTalk.data.remote.region

import com.kosim97.mulgaTalkTalk.data.api.ApiResult
import com.kosim97.mulgaTalkTalk.data.remote.model.ApiData
import com.kosim97.mulgaTalkTalk.data.repository.region.RegionRemoteDataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RegionService : RegionRemoteDataSource {
    @GET("ListNecessariesPricesService/{START_INDEX}/{END_INDEX}/ / / / /{M_GU_NAME}")
    override suspend fun getSampleRegion(
        @Path("START_INDEX") start: Int, @Path("END_INDEX") end: Int, @Path("M_GU_NAME") region: String
    ): Response<ApiData>

    @GET("ListNecessariesPricesService/{START_INDEX}/{END_INDEX}/ / / / /{M_GU_NAME}")
    override suspend fun getRegionDetail(
        @Path("START_INDEX") start: Int, @Path("END_INDEX") end: Int, @Path("M_GU_NAME") region: String
    ): Response<ApiData>
}