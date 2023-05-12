package com.kosim97.mulgaTalkTalk.ui.home.detail

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.kosim97.mulgaTalkTalk.data.paging.RegionPaging
import com.kosim97.mulgaTalkTalk.data.remote.model.ResultData
import com.kosim97.mulgaTalkTalk.data.repository.region.RegionRepository
import com.kosim97.mulgaTalkTalk.di.AppDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeDetailViewModel @Inject constructor(
    private val repository: RegionRepository,
    @AppDate
    private val appDate: String,
    sharedPref: SharedPreferences
) : ViewModel() {
    private val _backBtn = MutableSharedFlow<Boolean>(replay = 0)
    val backBtn: SharedFlow<Boolean>
        get() = _backBtn

    val isEmpty = MutableStateFlow(false)

    var mRegion: String = ""
    val date = sharedPref.getString("KEY_API_DATE", appDate)
    val updateText = "업데이트 날짜: $date"
    fun initData(region: String) {
        mRegion = region
    }

    fun getDataList(): Flow<PagingData<ResultData>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { RegionPaging(repository, mRegion, date!!, isEmpty) }
        ).flow
    }

    fun clickBackBtn() {
        viewModelScope.launch {
            _backBtn.emit(true)
        }
    }
}