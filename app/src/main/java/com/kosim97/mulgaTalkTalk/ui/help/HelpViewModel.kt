package com.kosim97.mulgaTalkTalk.ui.help

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
import com.kosim97.mulgaTalkTalk.R
import com.kosim97.mulgaTalkTalk.data.local.model.HelpData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HelpViewModel @Inject constructor(
    val app: Application,
    private val firebaseDB: FirebaseDatabase
): ViewModel() {
    val productList = ArrayList<HelpData>()
    val rotateItemList = ArrayList<HelpData>()
    private val _backBtn = MutableSharedFlow<Boolean>(replay = 0)
    val backBtn: SharedFlow<Boolean>
        get() = _backBtn
    private val _rotateItem = MutableSharedFlow<List<HelpData>>(replay = 0)
    val rotateItem: SharedFlow<List<HelpData>>
        get() = _rotateItem

    fun initProductList() {
        productList.clear()
        app.resources.getStringArray(R.array.product_array).forEach {
            productList.add(HelpData(it))
        }
    }

    fun clickBackBtn() {
        viewModelScope.launch {
            _backBtn.emit(true)
        }
    }

    fun getFirebaseHelpItem() {
        val myRef = firebaseDB.getReference("rotate_info")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    rotateItemList.add(HelpData(it.value.toString()))
                    viewModelScope.launch(Dispatchers.IO) {
                        _rotateItem.emit(rotateItemList)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}