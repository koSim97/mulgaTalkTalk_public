package com.kosim97.mulgaTalkTalk.data.repository.all

import com.kosim97.mulgaTalkTalk.data.remote.model.ApiData
import retrofit2.Response

interface AllRemoteDataSource {
    suspend fun getAllData(start: Int, end: Int): Response<ApiData>
}