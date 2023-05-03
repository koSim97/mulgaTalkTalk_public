package com.kosim97.mulgaTalkTalk.ui.home.chart

import android.util.Log
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

    fun getFiveMonthData(region: String, product: String) {
        val splitDate = appDate.split("-").map { it }
        val cal = Calendar.getInstance()
        var end = 5
        cal.set(splitDate[0].toInt(), splitDate[1].toInt(),1)

        viewModelScope.launch(Dispatchers.IO) {
            chartList.clear()
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
                                }
                            }
                            else -> {

                            }
                        }
                    }
                if (cal.get(Calendar.MONTH) == 1) {
                    cal.add(Calendar.YEAR, -1)
                    cal.add(Calendar.MONTH, 11)
                } else {
                    cal.add(Calendar.MONTH, -1)
                }
            }
            _chartDataList.emit(chartList.reversed())
            _labelText.emit("$region $product")
        }
    }
}