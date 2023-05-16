package com.kosim97.mulgaTalkTalk.ui.home.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kosim97.mulgaTalkTalk.ui.common.CommonPopup
import com.kosim97.mulgaTalkTalk.ui.common.LoadingDialog
import com.kosim97.mulgaTalkTalk.R
import com.kosim97.mulgaTalkTalk.databinding.ActivityHomeDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeDetailBinding
    private val homeDetailViewModel: HomeDetailViewModel by viewModels()
    private val detailAdapter: HomeDetailAdapter = HomeDetailAdapter()
    lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        initView()
        initObserver()
    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_detail)
        binding.also {
            it.lifecycleOwner = this
            it.viewModel = homeDetailViewModel
        }

        binding.homeDetailList.apply {
            adapter = detailAdapter
        }

        val region = intent.getStringExtra("region")
        if (region != null) {
            homeDetailViewModel.initData(region)
        }
        loadingDialog = LoadingDialog(this)
        loadingDialog.show()
    }

    private fun initObserver() {
        lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeDetailViewModel.getDataList().collect { pagingData ->
                    detailAdapter.submitData(pagingData)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailAdapter.loadStateFlow.collectLatest { loadStates ->

                    if (loadStates.source.refresh is LoadState.Error) {
                        showNetworkErrorPopup()
                    } else {
                        when (loadStates.append) {
                            is LoadState.Loading -> {
                                loadingDialog.show()
                            }
                            else -> {
                                loadingDialog.dismiss()
                            }
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeDetailViewModel.backBtn.collect {
                    if (it) {
                        finish()
                        return@collect
                    }
                }
            }
        }
    }

    private fun showNetworkErrorPopup() {
        val popup = CommonPopup(
            this,
            CommonPopup.ButtonType.ONE,
            getString(R.string.network_error_txt),
            "확인"
        )
        popup.setLeftBtnOnclickListener {
            popup.dismiss()
            ActivityCompat.finishAffinity(this)
        }
        popup.setCancelable(false)
        popup.show()
    }
}