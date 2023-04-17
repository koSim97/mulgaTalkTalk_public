package com.kosim97.mulgaTalkTalk.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoomEntity::class], version = 1)
abstract class FavoriteDB :RoomDatabase(){
    abstract fun roomDao() : RoomInterface
}