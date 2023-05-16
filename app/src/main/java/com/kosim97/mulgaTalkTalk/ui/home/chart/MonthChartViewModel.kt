package com.kosim97.mulgaTalkTalk.ui.home.chart

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosim97.mulgaTalkTalk.data.api.ApiResult
import com.kosim97.mulgaTalkTalk.data.local.model.ChartData
import com.kosim97.mulgaTalkTalk.data.repository.product.ProductRepository
import com.kosim97.mulgaTalkTalk.di.AppDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class MonthChartViewModel @Inject constructor(
    private val repository: ProductRepository,
    @AppDate
    private val appDate: String,
): ViewModel() {
    private val chartList = mutableListOf<ChartData>()
    private val _chartDataList = MutableSharedFlow<List<ChartData>>(0)
    val chartDataList: SharedFlow<List<ChartData>>
        get() = _chartDataList
    private val _labelText = MutableStateFlow("")
    val labelText: StateFlow<String>
        get() = _labelText
    private val _loading = MutableSharedFlow<Boolean>(0)
    val loading: SharedFlow<Boolean>
        get() = _loading
    private val _backBtn = MutableSharedFlow<Boolean>(0)
    val backBtn: SharedFlow<Boolean>
        get() = _backBtn
    private val _dataEmpty = MutableSharedFlow<Boolean>(0)
    val dataEmpty: SharedFlow<Boolean>
        get() = _dataEmpty

    val isEmpty = MutableStateFlow(false)

    fun getFiveMonthData(region: String, product: String) {
        val splitDate = appDate.split("-").map { it }
        val cal = Calendar.getInstance()
        var end = 5
        var cnt = 0
        cal.set(splitDate[0].toInt(), splitDate[1].toInt(),1)

        viewModelScope.launch(Dispatchers.IO) {
            _loading.emit(true)
            chartList.clear()
            if (region.isEmpty() || product.isEmpty()) {
                _dataEmpty.emit(true)
            } else {
                while (end != 0) {
                    val date = SimpleDateFormat("yyyy-MM").format(cal.time)
                    repository.getProductDetail(1,1, region, product, date)
                        .flowOn(Dispatchers.IO)
                        .collect{
                            when(it) {
                                is ApiResult.Success -> {
                                    val price = it.data?.list?.row?.get(0)?.productPrice?.toInt()
                                    if (price != null) {
                                        chartList.add(ChartData(date, price))
                                        end--
                                    } else {
                                        cnt++
                                    }
                                }
                                else -> {
                                    cnt++
                                }
                            }
                        }
                    if (cal.get(Calendar.MONTH) == 1) {
                        cal.add(Calendar.YEAR, -1)
                        cal.add(Calendar.MONTH, 11)
                    } else {
                        cal.add(Calendar.MONTH, -1)
                    }
                    if (cnt > 5) {
                        isEmpty.emit(true)
                        break
                    } else {
                        isEmpty.emit(false)
                    }
                }
                _chartDataList.emit(chartList.reversed())
                _labelText.emit("$region $product")
                _loading.emit(false)
            }
        }
    }

    fun clickBackBtn() {
        viewModelScope.launch(Dispatchers.Main) {
            _backBtn.emit(true)
        }
    }
}