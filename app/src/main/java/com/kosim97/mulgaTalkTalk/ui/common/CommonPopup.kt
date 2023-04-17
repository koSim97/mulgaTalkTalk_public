package com.kosim97.mulgaTalkTalk.ui.common

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kosim97.mulgaTalkTalk.R
import kotlin.properties.Delegates

class CommonPopup(
    private val mContext: Context,
    private val buttonType: ButtonType,
    private val content: String,
    private val mBtn2Name: String
) : Dialog(mContext, R.style.DialogTheme) {

    constructor(
        mContext: Context,
        buttonType: ButtonType,
        content: String,
        btn1Name: String,
        btn2Name: String
    ) : this(mContext, buttonType, content, btn2Name) {
        this.mBtn1Name = btn1Name
    }

    enum class ButtonType {
        ONE, TWO
    }

    private var btn1OnclickListener by Delegates.notNull<View.OnClickListener>()
    private var btn2OnclickListener by Delegates.notNull<View.OnClickListener>()
    private var btn1 by Delegates.notNull<Button>()
    private var btn2 by Delegates.notNull<Button>()
    private var mContent by Delegates.notNull<TextView>()
    private var mBtn1Name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    mContext,
                    R.color.dialog_bg
                )
            )
        )
        setContentView(R.layout.layout_popup)
        setButton()
    }

    fun setLeftBtnOnclickListener(onSingleClick: View.OnClickListener) {
        btn1OnclickListener = onSingleClick
    }

    fun setRightBtn2OnclickListener(onSingleClick: View.OnClickListener) {
        btn2OnclickListener = onSingleClick
    }

    private fun setButton() {
        btn1 = findViewById(R.id.btn1)
        btn2 = findViewById(R.id.btn2)
        mContent = findViewById(R.id.tvContent)

        when (buttonType) {
            ButtonType.ONE -> {
                btn1.visibility = View.GONE
                btn2.setOnClickListener {
                    dismiss()
                    btn1OnclickListener.onClick(it)
                }
            }
            ButtonType.TWO -> {
                btn1.setOnClickListener {
                    dismiss()
                    btn1OnclickListener.onClick(it)
                }
                btn2.setOnClickListener {
                    dismiss()
                    btn2OnclickListener.onClick(it)
                }
            }
        }
        mContent.text = content
        btn1.text = mBtn1Name
        btn2.text = mBtn2Name
    }
}