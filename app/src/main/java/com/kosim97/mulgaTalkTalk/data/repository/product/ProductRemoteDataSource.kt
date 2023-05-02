package com.kosim97.mulgaTalkTalk.data.repository.product

import com.kosim97.mulgaTalkTalk.data.remote.model.ApiData
import retrofit2.Response
import retrofit2.http.GET

interface ProductRemoteDataSource {

    suspend fun getProductDetail(
        start: Int, end: Int, region: String, product: String, date: String
    ): Response<ApiData>
}