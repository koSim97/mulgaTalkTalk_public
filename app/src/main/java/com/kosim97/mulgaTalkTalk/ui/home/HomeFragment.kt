package com.kosim97.mulgaTalkTalk.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kosim97.mulgaTalkTalk.R
import com.kosim97.mulgaTalkTalk.data.local.model.AutoSlideData
import com.kosim97.mulgaTalkTalk.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private var isInitView = false
    private lateinit var slideAdapter: AutoSlideAdapter

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
        val slideItem = mutableListOf<AutoSlideData>()
        slideItem.add(AutoSlideData("test"))
        slideItem.add(AutoSlideData("test1"))
        slideAdapter = AutoSlideAdapter()
        binding.autoSlide.adapter = slideAdapter
        slideAdapter.submitList(slideItem)
        homeViewModel.test()
        binding.navigateChart.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_monthChartFragment)
        }
    }
}