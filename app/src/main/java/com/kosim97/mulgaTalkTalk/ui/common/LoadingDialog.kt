package com.kosim97.mulgaTalkTalk.ui.common

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import com.airbnb.lottie.LottieAnimationView
import com.kosim97.mulgaTalkTalk.R

class LoadingDialog(
    context: Context
) : Dialog(context, R.style.LoadingDialog) {
    private var isAttached = false

    init {
        setContentView(R.layout.dialog_loading)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        isAttached = true
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        isAttached = false
    }

    override fun show() {
        if (window != null) {
            val wlp = window!!.attributes
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            window!!.attributes = wlp
        }

        super.show()
        val animation = findViewById<LottieAnimationView>(R.id.loading_image)
        animation.playAnimation()
    }
}