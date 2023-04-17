package com.kosim97.mulgaTalkTalk.data.api

import com.kosim97.mulgaTalkTalk.data.HomeDetail
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("ListNecessariesPricesService/{START_INDEX}/{END_INDEX}")
    suspend fun getAllApi(
        @Path("START_INDEX") start: Int, @Path("END_INDEX") end: Int
    ): Response<HomeDetail>

    @GET("ListNecessariesPricesService/{START_INDEX}/{END_INDEX}/ / / / /{M_GU_NAME}")
    suspend fun getRegionApi(
        @Path("START_INDEX") start: Int,
        @Path("END_INDEX") end: Int,
        @Path("M_GU_NAME") region: String,
    ): Response<HomeDetail>

    @GET("ListNecessariesPricesService/{START_INDEX}/{END_INDEX}/ /{A_NAME}/{P_YEAR_MONTH}/ /{M_GU_NAME}")
    suspend fun getSearchApi(
        @Path("START_INDEX") start: Int,
        @Path("END_INDEX") end: Int,
        @Path("M_GU_NAME") region: String,
        @Path("A_NAME") p_name: String,
        @Path("P_YEAR_MONTH") date: String
    ): Response<HomeDetail>
}