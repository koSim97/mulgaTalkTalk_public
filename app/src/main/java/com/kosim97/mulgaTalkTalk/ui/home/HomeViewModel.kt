package com.kosim97.mulgaTalkTalk.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kosim97.mulgaTalkTalk.data.HomeData
import com.kosim97.mulgaTalkTalk.R
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
    private val firebaseDB: FirebaseDatabase
) : ViewModel() {
    val item = ArrayList<HomeData>()
    private val img = app.resources.obtainTypedArray(R.array.img_array)

    private val slideList = mutableListOf<String>()
    private val _slideDataList = MutableSharedFlow<List<String>>(0)
    val slideDataList: SharedFlow<List<String>>
        get() = _slideDataList
    private val _cancelLoading = MutableSharedFlow<Boolean>(0)
    val cancelLoading: SharedFlow<Boolean>
        get() = _cancelLoading

    fun initData() {
        item.clear()
        app.resources.getStringArray(R.array.region_array).forEachIndexed { index, s ->
            item.add(HomeData(s, img.getDrawable(index)!!))
        }
    }

    fun getFirebase() {
        val myRef = firebaseDB.getReference("url_info")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    slideList.add(it.value.toString())
                }
                viewModelScope.launch(Dispatchers.IO){
                    _slideDataList.emit(slideList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                viewModelScope.launch {
                    _cancelLoading.emit(true)
                }
            }
        })
    }
}