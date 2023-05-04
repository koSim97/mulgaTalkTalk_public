package com.kosim97.mulgaTalkTalk.ui.home.chart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.kosim97.mulgaTalkTalk.databinding.FragmentMonthChartBinding
import com.kosim97.mulgaTalkTalk.ui.common.LoadingDialog
import com.kosim97.mulgaTalkTalk.util.NavigationUtil
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
    private lateinit var lineDataSet: LineDataSet
    private var loadingDialog: LoadingDialog? = null

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
        initView()
    }

    override fun onDestroyView() {
        (requireActivity() as NavigationUtil).visibleNav(true)
        super.onDestroyView()
    }

    private fun initView() {
        loadingDialog = LoadingDialog(requireContext())
        (requireActivity() as NavigationUtil).visibleNav(false)
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
            it.setNoDataText("")
            it.extraRightOffset = 20f
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
        lineDataSet = LineDataSet(null,"")
        lineDataSet.setDrawValues(true)
        lineDataSet.lineWidth = 5f
        lineDataSet.valueTextSize = 15f

        lineData = LineData(lineDataSet)
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                chartViewModel.chartDataList.collectLatest{
                    var index = 1
                    clearData()
                    it.onEach { item ->
                        label.add(item.date)
                        lineData.addEntry(Entry(index.toFloat(), item.price.toFloat()), 0)
                        index++
                    }
                    binding.monthChart.let {chart ->
                        chart.xAxis.valueFormatter = IndexAxisValueFormatter(label)
                        chart.data = lineData
                        chart.moveViewToX(lineData.entryCount.toFloat())
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                chartViewModel.labelText.collectLatest {
                    lineDataSet.label = it
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                chartViewModel.loading.collectLatest {
                    showDialog(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                chartViewModel.backBtn.collectLatest {

                    findNavController().popBackStack()
                }
            }
        }
    }



    private fun clearData() {
        label.clear()
        label.add("")
        binding.monthChart.data?.clearValues()
        lineDataSet.clear()
        lineData.clearValues()
        binding.monthChart.clear()
        lineData = LineData(lineDataSet)
        lineData.notifyDataChanged()
        lineDataSet.notifyDataSetChanged()
        binding.monthChart.notifyDataSetChanged()
        binding.monthChart.invalidate()
    }

    private fun showDialog(show: Boolean) {
        if (show) {
            loadingDialog?.show()
        } else {
            loadingDialog?.dismiss()
        }
    }
}