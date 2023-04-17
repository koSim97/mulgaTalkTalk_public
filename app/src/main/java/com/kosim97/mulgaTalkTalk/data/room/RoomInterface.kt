package com.kosim97.mulgaTalkTalk.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomInterface {
    @Query("SELECT * FROM favorite")
    suspend fun getAll(): List<RoomEntity>

    @Query("SELECT * FROM favorite order by room_no ASC LIMIT :index OFFSET :loadSize")
    suspend fun getPage(index: Int, loadSize: Int): List<RoomEntity>

    // 중복 값 충돌 발생 시 새로 들어온 데이터로 교체.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFavorite(favoriteEntity: RoomEntity)

    @Delete
    suspend fun deleteFavorite(favoriteEntity: RoomEntity)

    @Query("DELETE FROM favorite WHERE room_no =:no")
    suspend fun deleteNo(no: Int)
}