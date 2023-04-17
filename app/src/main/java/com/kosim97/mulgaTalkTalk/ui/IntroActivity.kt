package com.kosim97.mulgaTalkTalk.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.kosim97.mulgaTalkTalk.MainActivity
import com.kosim97.mulgaTalkTalk.data.api.ApiResult
import com.kosim97.mulgaTalkTalk.data.repository.ApiRepository
import com.kosim97.mulgaTalkTalk.di.AppDate
import com.kosim97.mulgaTalkTalk.ui.common.CommonPopup
import com.kosim97.mulgaTalkTalk.ui.common.LoadingDialog
import com.kosim97.mulgaTalkTalk.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {
    @Inject
    lateinit var apiRepository: ApiRepository
    @Inject
    lateinit var sharedPref: SharedPreferences
    @AppDate
    @Inject
    lateinit var mDate: String
    lateinit var loadingDialog: LoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        supportActionBar?.hide()
        loadingDialog = LoadingDialog(this)
        lifecycleScope.launch {
            if (!isNetworkAvailable()) {
                showNetworkErrorPopup()
            } else {
                getApiData()
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false

        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    private suspend fun getApiData() {
        loadingDialog.show()
        apiRepository.getAllDetail(1, 1)
            .flowOn(Dispatchers.IO)
            .collect {
                when (it) {
                    is ApiResult.Success -> {
                        val date = it.data?.list?.row?.get(0)?.updateMonth
                        if (date != null && date != mDate) {
                            Log.d("test", "$date, $mDate")
                            sharedPref.edit().putString("KEY_API_DATE", date).apply()
                        }
                        loadingDialog.dismiss()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    else -> {
                        loadingDialog.dismiss()
                        showNetworkErrorPopup()
                        Log.d("test", "fail $it")
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