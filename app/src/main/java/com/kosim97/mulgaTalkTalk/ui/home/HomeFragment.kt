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
import com.kosim97.mulgaTalkTalk.R
import com.kosim97.mulgaTalkTalk.data.local.model.AutoSlideData
import com.kosim97.mulgaTalkTalk.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private var isInitView = false
    private lateinit var slideAdapter: AutoSlideAdapter
    private val slideItem = mutableListOf<AutoSlideData>()

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
        if (!isInitView) {
            homeViewModel.initData()
            isInitView = true
        }
        slideAdapter = AutoSlideAdapter()
        binding.autoSlide.adapter = slideAdapter
        initObserver()
        homeViewModel.getFirebase()
        binding.navigateChart.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_monthChartFragment)
        }
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.slideDataList.collect {
                    Log.d("test","asd $it")
                    setSlideItem(it)
                }
            }
        }
    }

    private fun setSlideItem(data: List<String>) {
        data.onEach {
            slideItem.add(AutoSlideData(it))
        }
        slideAdapter.submitList(slideItem)
    }
}