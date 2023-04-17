package com.kosim97.mulgaTalkTalk.di

import android.content.Context
import androidx.room.Room
import com.kosim97.mulgaTalkTalk.data.room.FavoriteDB
import com.kosim97.mulgaTalkTalk.data.room.RoomInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesAppDB(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
            context,
            FavoriteDB::class.java,
            "favorite.db"
        ).build()

    @Provides
    @Singleton
    fun providesDAO(
        favoriteDB: FavoriteDB
    ): RoomInterface = favoriteDB.roomDao()
}