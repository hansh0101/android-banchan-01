package co.kr.woowahan_banchan.presentation.ui.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import co.kr.woowahan_banchan.databinding.DialogCartAddBottomBinding
import co.kr.woowahan_banchan.domain.entity.dish.Dish
import co.kr.woowahan_banchan.presentation.viewmodel.bottomsheet.BottomSheetViewModel
import co.kr.woowahan_banchan.util.ImageLoader
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartAddBottomSheet : BottomSheetDialogFragment() {

    private var _binding: DialogCartAddBottomBinding? = null
    private val binding get() = _binding ?: error("binding not initialized")

    private val viewModel by viewModels<BottomSheetViewModel>()

    private var dish: Dish? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogCartAddBottomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        initViewData()
        setListener()
    }

    private fun initViewData() {
        dish?.let {
            ImageLoader.loadImage(it.imageUrl) { bitmap ->
                binding.ivImage.setImageBitmap(bitmap)
            }
            viewModel.setCurrentDish(it)
        }
    }

    private fun setListener() {
        binding.tvCancel.setOnClickListener {
            dialog?.dismiss()
        }
    }

    companion object {
        fun newInstance(dish: Dish): CartAddBottomSheet {
            return CartAddBottomSheet().apply {
                this.dish = dish
            }
        }
    }
}

