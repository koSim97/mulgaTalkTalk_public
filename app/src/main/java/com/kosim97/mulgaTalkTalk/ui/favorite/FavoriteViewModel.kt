package com.kosim97.mulgaTalkTalk.ui.favorite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.kosim97.mulgaTalkTalk.data.paging.FavoriteDBPaging
import com.kosim97.mulgaTalkTalk.data.room.RoomInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    val roomInterface: RoomInterface
): ViewModel() {
    fun getRoomData() = Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false, initialLoadSize = 20),
            pagingSourceFactory = { FavoriteDBPaging(roomInterface) }
        ).flow

    fun getAll() {
        viewModelScope.launch {
            Log.d("test","${roomInterface.getAll()}")
        }
    }
}