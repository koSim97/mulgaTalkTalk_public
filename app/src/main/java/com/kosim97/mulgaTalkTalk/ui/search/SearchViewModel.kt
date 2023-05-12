package com.kosim97.mulgaTalkTalk.ui.search

import android.app.Application
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.kosim97.mulgaTalkTalk.data.room.RoomEntity
import com.kosim97.mulgaTalkTalk.data.room.RoomInterface
import com.kosim97.mulgaTalkTalk.data.paging.ProductPaging
import com.kosim97.mulgaTalkTalk.data.remote.model.ResultData
import com.kosim97.mulgaTalkTalk.data.repository.product.ProductRepository
import com.kosim97.mulgaTalkTalk.di.AppDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ProductRepository,
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

    private val _isSameItem = MutableSharedFlow<Boolean>(replay = 0)
    val isSameItem: SharedFlow<Boolean>
        get() = _isSameItem

    fun getSearchData(): Flow<PagingData<ResultData>> {
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
            var save = true
            viewModelScope.launch(Dispatchers.IO) {
                val list = roomInterface.getAll()
                for (i in list.indices) {
                    if (list[i].room_product == saveProduct && list[i].room_region == saveRegion) {
                        save = false
                        _isSameItem.emit(false)
                    }
                }
                if (save) {
                    _isSameItem.emit(true)
                    roomInterface.saveFavorite(RoomEntity(saveRegion, saveProduct))
                }
            }
        }
    }
}