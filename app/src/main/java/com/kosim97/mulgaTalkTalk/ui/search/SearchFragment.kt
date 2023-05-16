package com.kosim97.mulgaTalkTalk.ui.search

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kosim97.mulgaTalkTalk.ui.common.CommonPopup
import com.kosim97.mulgaTalkTalk.ui.common.LoadingDialog
import com.kosim97.mulgaTalkTalk.R
import com.kosim97.mulgaTalkTalk.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var searchBinding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by viewModels()
    private val searchAdapter: SearchAdapter = SearchAdapter()
    private var loadingDialog: LoadingDialog? = null
    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mContext = requireContext()
        FragmentSearchBinding.inflate(inflater).also {
            searchBinding = it
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = searchViewModel
            return it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
    }

    @SuppressLint("DiscouragedPrivateApi")
    private fun initView() {
        searchBinding.searchRv.also {
            it.adapter = searchAdapter
        }
        searchBinding.helpBtn.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_search_to_helpFragment)
        }
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            searchViewModel.searchBtn.collectLatest { click ->
                if (click) {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        searchViewModel.getSearchData().collectLatest {
                            searchAdapter.submitData(it)
                        }
                    }
                    return@collectLatest
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loadingDialog = LoadingDialog(mContext)
                searchAdapter.loadStateFlow.collectLatest { loadStates ->
                    if (loadStates.source.refresh is LoadState.Error) {
                        showNetworkErrorPopup()
                    } else {
                        when (loadStates.append) {
                            is LoadState.Loading -> {
                                loadingDialog?.show()
                            }
                            else -> {
                                loadingDialog?.dismiss()
                            }
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.isSameItem.collectLatest {
                    if (it) {
                        Toast.makeText(mContext, "저장되었어요.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(mContext, "이미 있는 즐겨찾기에요.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showNetworkErrorPopup() {
        val popup = CommonPopup(
            mContext,
            CommonPopup.ButtonType.ONE,
            getString(R.string.network_error_txt),
            "확인"
        )
        popup.setLeftBtnOnclickListener {
            popup.dismiss()
            ActivityCompat.finishAffinity(mContext as Activity)
        }
        popup.setCancelable(false)
        popup.show()
    }
}