package com.kosim97.mulgaTalkTalk.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.kosim97.mulgaTalkTalk.data.HomeData
import com.kosim97.mulgaTalkTalk.R
import com.kosim97.mulgaTalkTalk.data.api.ApiResult
import com.kosim97.mulgaTalkTalk.data.local.model.ChartData
import com.kosim97.mulgaTalkTalk.data.repository.region.RegionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val app: Application,
    private val repository: RegionRepository
) : ViewModel() {
    val item = ArrayList<HomeData>()
    private val img = app.resources.obtainTypedArray(R.array.img_array)

    val slideItem = MutableStateFlow("")
    private val slideList = mutableListOf<String>()
    private val _slideDataList = MutableSharedFlow<List<String>>(0)
    val slideDataList: SharedFlow<List<String>>
        get() = _slideDataList

    fun initData() {
        item.clear()
        app.resources.getStringArray(R.array.region_array).forEachIndexed { index, s ->
            item.add(HomeData(s, img.getDrawable(index)!!))
        }
    }

    fun getFirebase() {
        val database = Firebase.database
        val myRef = database.getReference("url_info")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    Log.d("test","123 ${it.value}")
                    slideList.add(it.value.toString())
                }
                viewModelScope.launch(Dispatchers.IO){
                    _slideDataList.emit(slideList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("test","fail")
            }

        })
    }
}