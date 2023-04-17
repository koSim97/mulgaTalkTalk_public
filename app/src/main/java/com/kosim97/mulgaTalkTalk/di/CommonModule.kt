package com.kosim97.mulgaTalkTalk.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.text.SimpleDateFormat
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Singleton
    @Provides
    fun getShared(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences("api_date", 0)
    }

    @AppDate
    @Singleton
    @Provides
    fun setDate(
        sharedPref: SharedPreferences
    ): String {
        return sharedPref.getString(
            "KEY_API_DATE",
            SimpleDateFormat("yyyy-MM").format(System.currentTimeMillis())
        )!!
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AppDate