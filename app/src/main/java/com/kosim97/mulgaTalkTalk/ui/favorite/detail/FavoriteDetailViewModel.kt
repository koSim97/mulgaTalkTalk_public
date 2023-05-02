package com.kosim97.mulgaTalkTalk.ui.favorite.detail

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kosim97.mulgaTalkTalk.data.paging.ProductPaging
import com.kosim97.mulgaTalkTalk.data.remote.model.ResultData
import com.kosim97.mulgaTalkTalk.data.repository.product.ProductRepository
import com.kosim97.mulgaTalkTalk.data.room.RoomInterface
import com.kosim97.mulgaTalkTalk.di.AppDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteDetailViewModel @Inject constructor(
    private val repository: ProductRepository,
    @AppDate
    private val appDate: String,
    sharedPref: SharedPreferences,
    private val database: RoomInterface
): ViewModel() {
    private val _backBtn = MutableSharedFlow<Boolean>(replay = 0)
    val backBtn: SharedFlow<Boolean>
        get() = _backBtn

    val isEmpty = MutableStateFlow(false)
    val date = sharedPref.getString("KEY_API_DATE", appDate)

    private var mRegion: String = ""
    private var mProduct: String = ""
    private var mNo: Int = 0
    var title: String = ""

    fun initData(region: String, product: String, no: Int) {
        mRegion = region
        mProduct = product
        mNo = no
        title = "$mRegion $mProduct"
    }

    fun getFavoriteDetailData(): Flow<PagingData<ResultData>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { ProductPaging(repository, mRegion, mProduct, date!!, isEmpty)}
        ).flow
    }

    fun clickBackBtn() {
        viewModelScope.launch {
            _backBtn.emit(true)
        }
    }

    fun deleteDB() {
        viewModelScope.launch(Dispatchers.IO) {
            database.deleteNo(mNo)
            _backBtn.emit(true)
        }
    }
}