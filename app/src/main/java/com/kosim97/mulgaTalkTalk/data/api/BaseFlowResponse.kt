package com.kosim97.mulgaTalkTalk.data.api

import retrofit2.Response

open class BaseFlowResponse() {
    fun <T : Any> flowCall(response: Response<T>): ApiResult<T> {
        return when (response.isSuccessful) {
            true -> {
                ApiResult.Success(response.body())
            }
            else -> {
                ApiResult.Fail(response.message())
            }
        }
    }
}