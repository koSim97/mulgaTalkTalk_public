package com.kosim97.mulgaTalkTalk.data.api

sealed class ApiResult<out T : Any> {
    class Success<out T : Any>(val data: T?) : ApiResult<T>()
    class Fail(val code: String) : ApiResult<Nothing>()
}

data class ApiFailRetryData(
    val result: ApiResult.Fail,
    val retry: () -> Unit
)