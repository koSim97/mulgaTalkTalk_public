package com.kosim97.mulgaTalkTalk.ui.home.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.kosim97.mulgaTalkTalk.databinding.FragmentMonthChartBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MonthChartFragment : Fragment() {
    private lateinit var binding: FragmentMonthChartBinding
    private val chartViewModel : MonthChartViewModel by viewModels()
    private lateinit var lineData: LineData
    private val label = mutableListOf<String>()
    var index = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FragmentMonthChartBinding.inflate(inflater).also {
            binding = it
            it.lifecycleOwner = viewLifecycleOwner
            binding.viewModel = chartViewModel
            return it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            chartViewModel.getFiveMonthData()
        }
        initView()
    }

    private fun initView() {
        initChart()
        setChartData()
        initObserver()
    }

    private fun initChart() {
        binding.monthChart.let {
            it.description.isEnabled = false
            it.setTouchEnabled(false)
            it.isDragEnabled = false
            it.setScaleEnabled(false)
            it.setPinchZoom(false)
            it.setVisibleXRange(5f, 5f)
            it.axisRight.isEnabled = false
            it.legend.apply {
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                orientation = Legend.LegendOrientation.HORIZONTAL
                setDrawInside(false)
            }
            it.xAxis.let { x ->
                x.position = XAxis.XAxisPosition.BOTTOM
                x.textSize = 10f
                x.setDrawAxisLine(false)
                x.setDrawGridLines(false)
                x.granularity = 1f
            }
            it.axisLeft.let { y ->
                y.textSize = 10f
                y.setDrawGridLines(false)
                y.setDrawAxisLine(false)
                y.xOffset = 20f
            }
        }
    }

    private fun setChartData() {
        val lineDataSet= LineDataSet(null,"강남구 돼지고기")
        lineDataSet.setDrawValues(true)
        lineDataSet.lineWidth = 5f
        lineDataSet.valueTextSize = 15f

        lineData = LineData(lineDataSet)
        label.add("")
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                chartViewModel.chartDataList.collectLatest{
                    it.onEach { item ->
                        label.add(item.date)
                        lineData.addEntry(Entry(index.toFloat(), item.price.toFloat()), 0)
                        index++
                    }
                    binding.monthChart.let {chart ->
                        chart.xAxis.valueFormatter = IndexAxisValueFormatter(label)
                        chart.data = lineData
                        chart.notifyDataSetChanged()
                        chart.moveViewToX(lineData.entryCount.toFloat())
                    }
                }
            }
        }
    }
}