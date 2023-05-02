package com.kosim97.mulgaTalkTalk.data.remote.product

import com.kosim97.mulgaTalkTalk.data.remote.model.ApiData
import com.kosim97.mulgaTalkTalk.data.repository.product.ProductRemoteDataSource
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService: ProductRemoteDataSource {

    @GET("ListNecessariesPricesService/{START_INDEX}/{END_INDEX}/ /{A_NAME}/{P_YEAR_MONTH}/ /{M_GU_NAME}")
    override suspend fun getProductDetail(
        @Path("START_INDEX") start: Int,
        @Path("END_INDEX") end: Int,
        @Path("M_GU_NAME") region: String,
        @Path("A_NAME") product: String,
        @Path("P_YEAR_MONTH") date: String
    ): Response<ApiData>
}