package com.kosim97.mulgaTalkTalk.ui.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.kosim97.mulgaTalkTalk.data.local.model.HelpData

import com.kosim97.mulgaTalkTalk.databinding.FragmentHelpBinding
import com.kosim97.mulgaTalkTalk.util.NavigationUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HelpFragment : Fragment() {
    private lateinit var helpBinding: FragmentHelpBinding
    private val helpViewModel : HelpViewModel by viewModels()
    private lateinit var helpRotateAdapter: HelpRotateAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()
        FragmentHelpBinding.inflate(inflater).also {
            helpBinding = it
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = helpViewModel
            return it.root
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as NavigationUtil).visibleNav(false)
        initObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as NavigationUtil).visibleNav(true)

    }

    private fun initView() {
        helpViewModel.getFirebaseHelpItem()
        helpViewModel.initProductList()
        helpRotateAdapter = HelpRotateAdapter()
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                helpViewModel.backBtn.collectLatest {
                    if (it) {
                        findNavController().popBackStack()
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                helpViewModel.rotateItem.collectLatest {
                    setItem(it)
                }
            }
        }
    }

    private fun setItem(item: List<HelpData>) {
        helpBinding.productHelpRotateRv.adapter = helpRotateAdapter
        helpRotateAdapter.submitList(item.toMutableList())
    }
}