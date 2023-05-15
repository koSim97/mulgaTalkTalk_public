package com.kosim97.mulgaTalkTalk.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.kosim97.mulgaTalkTalk.R
import com.kosim97.mulgaTalkTalk.data.local.model.AutoSlideData
import com.kosim97.mulgaTalkTalk.databinding.FragmentHomeBinding
import com.kosim97.mulgaTalkTalk.ui.common.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private var isInitView = false
    private lateinit var slideAdapter: AutoSlideAdapter
    private val slideItem = mutableListOf<AutoSlideData>()
    private var slidePosition = 0

    private val slideFlow = MutableSharedFlow<Boolean>(replay = 0)
    private var loadingDialog: LoadingDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentHomeBinding.inflate(inflater).also {
            binding = it
            it.lifecycleOwner = viewLifecycleOwner
            binding.viewModel = homeViewModel
            return it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
    }

    override fun onResume() {
        super.onResume()
        Log.d("test","resume")
        viewLifecycleOwner.lifecycleScope.launch {
            slideFlow.emit(true)
        }
    }

    private fun initView() {
        if (!isInitView) {
            loadingDialog = LoadingDialog(requireContext())
            loadingDialog?.show()
            homeViewModel.initData()
            homeViewModel.getFirebase()
            isInitView = true
        }
        binding.navigateChart.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_monthChartFragment)
        }
        if (::slideAdapter.isInitialized) {
            binding.autoSlide.adapter = slideAdapter
        }
        setAutoSlide()
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.slideDataList.collect {
                    Log.d("test","asd $it")
                    setSlideItem(it)
                    loadingDialog?.dismiss()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                slideFlow.collectLatest {
                    if (it) {
                        delay(3000)
                        slidePosition++
                        binding.autoSlide.currentItem = slidePosition
                    }
                }
            }
        }
    }

    private fun setSlideItem(data: List<String>) {
        val img = this.resources.obtainTypedArray(R.array.slide_img_array)
        data.forEachIndexed { index, s ->
            slideItem.add(AutoSlideData(s, img.getDrawable(index)!!))
        }
        slideAdapter = AutoSlideAdapter()
        binding.autoSlide.adapter = slideAdapter
        slideAdapter.submitList(slideItem)
    }

    private fun setAutoSlide() {
        binding.autoSlide.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                slidePosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                when (state) {
                    ViewPager2.SCROLL_STATE_IDLE -> {
                        viewLifecycleOwner.lifecycleScope.launch {
                            slideFlow.emit(true)
                        }
                    }
                    ViewPager2.SCROLL_STATE_DRAGGING -> {
                        viewLifecycleOwner.lifecycleScope.launch {
                            slideFlow.emit(false)
                        }
                    }
                    ViewPager2.SCROLL_STATE_SETTLING -> { }
                }
            }
        })
    }
}
