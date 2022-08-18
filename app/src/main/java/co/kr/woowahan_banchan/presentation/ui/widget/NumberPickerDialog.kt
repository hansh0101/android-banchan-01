package co.kr.woowahan_banchan.presentation.ui.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.NumberPicker
import androidx.appcompat.widget.AppCompatButton
import co.kr.woowahan_banchan.databinding.DialogNumberPickerBinding

class NumberPickerDialog(
    context: Context,
    private val amount: Int,
    private val pickNumber: (Int) -> Unit) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DialogNumberPickerBinding.inflate(LayoutInflater.from(context), null, false)
        setContentView(binding.root)

        initView(binding.npAmount,binding.btnPick)
    }

    private fun initView(picker:NumberPicker, pickBtn : AppCompatButton){
        picker.minValue = MIN_AMOUNT
        picker.maxValue = MAX_AMOUNT
        picker.value = amount
        pickBtn.setOnClickListener {
            pickNumber(picker.value)
            dismiss()
        }
    }

    companion object{
        private const val MIN_AMOUNT = 1
        private const val MAX_AMOUNT = 100
    }
}