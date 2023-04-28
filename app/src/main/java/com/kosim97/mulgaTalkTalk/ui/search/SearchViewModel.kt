package com.kosim97.mulgaTalkTalk.ui.search

import android.app.Application
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.kosim97.mulgaTalkTalk.data.Detail
import com.kosim97.mulgaTalkTalk.data.room.RoomEntity
import com.kosim97.mulgaTalkTalk.data.room.RoomInterface
import com.kosim97.mulgaTalkTalk.data.paging.ProductPaging
import com.kosim97.mulgaTalkTalk.data.repository.ApiRepository
import com.kosim97.mulgaTalkTalk.di.AppDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ApiRepository,
    private val roomInterface: RoomInterface,
    @AppDate
    private val appDate: String,
    sharedPref: SharedPreferences,
    val app: Application
) : ViewModel() {
    var searchRegion = " "
    var searchProduct = " "

    val isEmpty = MutableStateFlow(false)
    private val date = sharedPref.getString("KEY_API_DATE", appDate)

    private val _searchBtn = MutableSharedFlow<Boolean>(replay = 0)
    val searchBtn: SharedFlow<Boolean>
        get() = _searchBtn

    fun getSearchData(): Flow<PagingData<Detail>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = {
                ProductPaging(
                    repository,
                    searchRegion,
                    searchProduct,
                    date!!,
                    isEmpty
                )
            }
        ).flow
    }

    fun clickSearchBtn(region: String, product: String) {
        searchRegion = region.ifEmpty { " " }
        searchProduct = product.ifEmpty { " " }
        viewModelScope.launch {
            _searchBtn.emit(true)
        }
    }

    fun saveFavorite(region: String, product: String) {
        val saveRegion = region.ifEmpty { " " }
        val saveProduct = product.ifEmpty { " " }
        if (saveRegion == " " && saveProduct == " ") {
            Toast.makeText(app, "지역과 물품을 정확히 입력해주세요.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(app, "저장되었습니다.", Toast.LENGTH_SHORT).show()
            viewModelScope.launch(Dispatchers.IO) {
                roomInterface.saveFavorite(RoomEntity(saveRegion, saveProduct))
            }
        }
    }
}