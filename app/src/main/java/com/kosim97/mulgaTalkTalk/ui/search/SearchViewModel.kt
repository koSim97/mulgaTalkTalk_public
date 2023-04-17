package com.kosim97.mulgaTalkTalk.ui.search

import android.app.Application
import android.content.SharedPreferences
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
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
    val regionItem = MutableStateFlow("")
    val productItem = MutableStateFlow("")

    val isEmpty = MutableStateFlow(false)
    val date = sharedPref.getString("KEY_API_DATE", appDate)

    private val _searchBtn = MutableSharedFlow<Boolean>(replay = 0)
    val searchBtn: SharedFlow<Boolean>
        get() = _searchBtn

    fun getSearchData(): Flow<PagingData<Detail>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = {
                ProductPaging(
                    repository,
                    regionItem.value,
                    productItem.value,
                    date!!,
                    isEmpty
                )
            }
        ).flow
    }

    fun clickSearchBtn() {
        viewModelScope.launch {
            _searchBtn.emit(true)
        }
    }

    fun saveFavorite() {
        Toast.makeText(app, "저장되었습니다.", Toast.LENGTH_SHORT).show()
        viewModelScope.launch(Dispatchers.IO) {
            roomInterface.saveFavorite(RoomEntity(regionItem.value, productItem.value))
        }
    }
}

object SearchRegionBindingAdapter {
    @InverseBindingAdapter(attribute = "selectedRegionItem", event = "selectRegionAttrChanged")
    @JvmStatic
    fun Spinner.getSelectedItem(): Any {
        return selectedItem
    }

    @BindingAdapter("selectRegionAttrChanged")
    @JvmStatic
    fun Spinner.setBindingListener(inverseBindingListener: InverseBindingListener) {
        inverseBindingListener.run {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (tag != position) {
                        inverseBindingListener.onChange()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }
    }

    @BindingAdapter("selectedRegionItem")
    @JvmStatic
    fun Spinner.setSelectedItem(selectedItem: Any) {
        adapter.run {
            val position = (adapter as ArrayAdapter<Any>).getPosition(selectedItem)
            setSelection(position, false)
            tag = position
        }
    }
}

object SearchProductBindingAdapter {
    @InverseBindingAdapter(attribute = "selectedProductItem", event = "selectProductAttrChanged")
    @JvmStatic
    fun Spinner.getSelectedItem(): Any {
        return selectedItem
    }

    @BindingAdapter("selectProductAttrChanged")
    @JvmStatic
    fun Spinner.setBindingListener(inverseBindingListener: InverseBindingListener) {
        inverseBindingListener.run {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (tag != position) {
                        inverseBindingListener.onChange()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }
    }

    @BindingAdapter("selectedProductItem")
    @JvmStatic
    fun Spinner.setSelectedItem(selectedItem: Any) {
        adapter.run {
            val position = (adapter as ArrayAdapter<Any>).getPosition(selectedItem)
            setSelection(position, false)
            tag = position
        }
    }
}