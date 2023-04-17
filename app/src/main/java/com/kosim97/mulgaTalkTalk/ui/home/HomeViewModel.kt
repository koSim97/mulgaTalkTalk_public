package com.kosim97.mulgaTalkTalk.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosim97.mulgaTalkTalk.data.HomeData
import com.kosim97.mulgaTalkTalk.R
import com.kosim97.mulgaTalkTalk.data.api.ApiResult
import com.kosim97.mulgaTalkTalk.data.repository.region.RegionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val app: Application,
    private val repository: RegionRepository
) : ViewModel() {
    val item = ArrayList<HomeData>()
    private val img = app.resources.obtainTypedArray(R.array.img_array)

    fun initData() {
        item.clear()
        app.resources.getStringArray(R.array.region_array).forEachIndexed { index, s ->
            item.add(HomeData(s, img.getDrawable(index)!!))
        }
    }

    fun test() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSampleRegion(1, 5, "강남구")
                .collect{
                    when(it) {
                        is ApiResult.Success -> {
                            Log.d("Test","asd ${it.data?.list?.row}")
                        }
                        else -> {

                        }
                    }
                }
        }
    }
}