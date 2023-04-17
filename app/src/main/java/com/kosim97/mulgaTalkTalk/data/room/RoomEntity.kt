package com.kosim97.mulgaTalkTalk.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class RoomEntity(
    val room_region: String,
    val room_product: String,
) {
    @PrimaryKey(autoGenerate = true)
    var room_no: Int = 0
}
