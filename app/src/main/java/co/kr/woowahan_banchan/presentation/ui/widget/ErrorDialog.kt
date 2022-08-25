package co.kr.woowahan_banchan.presentation.ui.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.core.view.isVisible
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.DialogRetryBinding
import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity

class ErrorDialog(
    context: Context,
    private val errorType: ErrorEntity,
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

        when (errorType) {
            is ErrorEntity.RetryableError -> {
                binding.tvTitle.text = "엥?"
                binding.tvSubtitle.text = "통신 중 문제가 발생했어요!"
                binding.btnRetry.isVisible = true
            }
            is ErrorEntity.ConditionalError -> {
                binding.tvTitle.text = "헉!"
                binding.tvSubtitle.text = "인터넷 연결을 확인해주세요!"
                binding.btnRetry.isVisible = true
            }
            is ErrorEntity.UnknownError -> {
                binding.tvTitle.text = "헐!"
                binding.tvSubtitle.text = "앗! 문제가 생겼어요!"
                binding.btnRetry.isVisible = false
            }
        }

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