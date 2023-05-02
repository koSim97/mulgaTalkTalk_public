package com.kosim97.mulgaTalkTalk.data.repository.product

import com.kosim97.mulgaTalkTalk.data.api.ApiResult
import com.kosim97.mulgaTalkTalk.data.api.BaseFlowResponse
import com.kosim97.mulgaTalkTalk.data.remote.model.ApiData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val remote: ProductRemoteDataSource
): BaseFlowResponse() {
    fun getProductDetail(
        start: Int, end: Int, region: String, product: String, date: String
    ): Flow<ApiResult<ApiData>> = flow {
        emit(flowCall(remote.getProductDetail(start, end, region, product, date)))
    }
}