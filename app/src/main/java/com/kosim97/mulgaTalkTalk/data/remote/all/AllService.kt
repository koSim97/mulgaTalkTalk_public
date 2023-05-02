package com.kosim97.mulgaTalkTalk.data.remote.all

import com.kosim97.mulgaTalkTalk.data.remote.model.ApiData
import com.kosim97.mulgaTalkTalk.data.repository.all.AllRemoteDataSource
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AllService: AllRemoteDataSource {

    @GET("ListNecessariesPricesService/{START_INDEX}/{END_INDEX}")
    override suspend fun getAllData(
        @Path("START_INDEX") start: Int, @Path("END_INDEX") end: Int
    ): Response<ApiData>
}