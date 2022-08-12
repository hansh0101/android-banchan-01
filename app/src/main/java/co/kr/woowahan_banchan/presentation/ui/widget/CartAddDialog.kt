package co.kr.woowahan_banchan.presentation.ui.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.DialogCartAddBinding
import co.kr.woowahan_banchan.util.shortToast

class CartAddDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DialogCartAddBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        )
        binding.tvPositive.setOnClickListener { dismiss() }
        binding.tvNegative.setOnClickListener { context.shortToast("장바구니 화면 이동 구현 요망") }
        this.setContentView(binding.root)

        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawableResource(R.drawable.inset_horizontal_24)
    }
}