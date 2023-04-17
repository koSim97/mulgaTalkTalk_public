package com.kosim97.mulgaTalkTalk.di

import android.content.Context
import android.view.View
import com.kosim97.mulgaTalkTalk.BuildConfig
import com.kosim97.mulgaTalkTalk.R
import com.kosim97.mulgaTalkTalk.data.api.ApiService
import com.kosim97.mulgaTalkTalk.data.remote.region.RegionService
import com.kosim97.mulgaTalkTalk.data.repository.region.RegionRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build()
        } else {
            OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
        }
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        @ApplicationContext context: Context
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://openAPI.seoul.go.kr:8088/${context.getString(R.string.api_key)}/json/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(
        retrofit: Retrofit
    ): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideRegionRemoteDataSource(
        retrofit: Retrofit
    ): RegionRemoteDataSource {
        return retrofit.create(RegionService::class.java)
    }
}