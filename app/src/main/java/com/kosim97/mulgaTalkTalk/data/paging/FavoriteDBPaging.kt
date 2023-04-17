package com.kosim97.mulgaTalkTalk.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kosim97.mulgaTalkTalk.data.room.RoomEntity
import com.kosim97.mulgaTalkTalk.data.room.RoomInterface

class FavoriteDBPaging(
    private val roomInterface: RoomInterface
) : PagingSource<Int, RoomEntity>() {
    override fun getRefreshKey(state: PagingState<Int, RoomEntity>): Int {
        return 0;
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RoomEntity> {
        return try {
            val key = params.key ?: 0
            val response = roomInterface.getPage(params.loadSize, key * params.loadSize)
            LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = if (response.isEmpty()) null else key + 1
            )
        }catch (e: Exception) {
            LoadResult.Error(e)
        }

    }
}