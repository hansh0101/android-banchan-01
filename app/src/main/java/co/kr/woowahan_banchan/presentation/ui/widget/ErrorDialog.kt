package co.kr.woowahan_banchan.presentation.ui.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.DialogRetryBinding

class ErrorDialog(
    context: Context,
    private val onClickRetry: () -> Unit = {},
    private val onClickCancel: () -> Unit = {}
) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DialogRetryBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        )

        with(binding) {
            btnRetry.setOnClickListener {
                onClickRetry()
                dismiss()
            }
            ivClose.setOnClickListener {
                onClickCancel()
                dismiss()
            }
        }

        setContentView(binding.root)
        setCanceledOnTouchOutside(false)
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawableResource(R.drawable.inset_horizontal_24)
    }
}