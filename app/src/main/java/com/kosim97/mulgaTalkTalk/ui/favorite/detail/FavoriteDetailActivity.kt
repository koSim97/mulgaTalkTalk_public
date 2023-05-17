package com.kosim97.mulgaTalkTalk.ui.favorite.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.kosim97.mulgaTalkTalk.ui.common.LoadingDialog
import com.kosim97.mulgaTalkTalk.R
import com.kosim97.mulgaTalkTalk.data.room.RoomInterface
import com.kosim97.mulgaTalkTalk.databinding.ActivityFavoriteDetailBinding
import com.kosim97.mulgaTalkTalk.ui.common.CommonPopup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteDetailActivity : AppCompatActivity() {
    private lateinit var favoriteDetailBinding: ActivityFavoriteDetailBinding
    private val favoriteDetailViewModel: FavoriteDetailViewModel by viewModels()
    private val favoriteDetailAdapter = FavoriteDetailAdapter()
    lateinit var loadingDialog: LoadingDialog
    @Inject
    lateinit var database: RoomInterface
    private var mNo = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initObserver()
    }

    private fun initView() {
        supportActionBar?.hide()

        favoriteDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_favorite_detail)
        favoriteDetailBinding.also {
            it.lifecycleOwner = this
            it.viewModel = favoriteDetailViewModel
        }

        val region: String? = intent.getStringExtra("region")
        val product: String? = intent.getStringExtra("product")
        mNo= intent.getIntExtra("no", 0)

        if (region != null) {
            if (product != null) {
                favoriteDetailViewModel.initData(region, product)
            }
        }

        favoriteDetailBinding.favoriteDetailRv.apply {
            adapter = favoriteDetailAdapter
        }
        loadingDialog = LoadingDialog(this)
    }

    private fun initObserver() {
        lifecycleScope.launch(Dispatchers.IO) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                favoriteDetailViewModel.getFavoriteDetailData().collectLatest {
                    favoriteDetailAdapter.submitData(it)
                }
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                favoriteDetailViewModel.backBtn.collect {
                    if (it) {
                        finish()
                        return@collect
                    }
                }
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                favoriteDetailAdapter.loadStateFlow.collectLatest { loadStates ->
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
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                favoriteDetailViewModel.showDelete.collectLatest {
                    if (it) {
                        showDeletePopup()
                    }
                }
            }
        }
    }

    private fun showDeletePopup() {
        val popup = CommonPopup(
            this,
            CommonPopup.ButtonType.TWO,
            getString(R.string.delete_favorite_popup),
            "취소",
            "확인"
        )
        popup.setLeftBtnOnclickListener {
            popup.dismiss()
        }
        popup.setRightBtn2OnclickListener {
            popup.dismiss()
            deleteFavorite()
        }
        popup.setCancelable(false)
        popup.show()
    }

    private fun deleteFavorite() {
        lifecycleScope.launch(Dispatchers.IO) {
            database.deleteNo(mNo)
            finish()
        }
    }
}