package co.kr.woowahan_banchan.presentation.ui.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import co.kr.woowahan_banchan.databinding.DialogCartAddBottomBinding
import co.kr.woowahan_banchan.domain.entity.dish.SelectedDish
import co.kr.woowahan_banchan.presentation.viewmodel.UiState
import co.kr.woowahan_banchan.presentation.viewmodel.bottomsheet.BottomSheetViewModel
import co.kr.woowahan_banchan.util.ImageLoader
import co.kr.woowahan_banchan.util.shortToast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CartAddBottomSheet : BottomSheetDialogFragment() {

    private var _binding: DialogCartAddBottomBinding? = null
    private val binding get() = _binding ?: error("binding not initialized")

    private val viewModel by viewModels<BottomSheetViewModel>()

    private var selectedDish: SelectedDish? = null

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
        observeData()
    }

    private fun initViewData() {
        selectedDish?.let {
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

    private fun observeData() {
        viewModel.isAddSuccess.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when(it){
                    is UiState.Success -> {
                        CartAddDialog(requireContext()).show()
                        dialog?.dismiss()
                    }
                    is UiState.Error -> {
                        requireContext().shortToast(it.message)
                        dialog?.dismiss()
                    }
                }
            }.launchIn(lifecycleScope)
    }

    companion object {
        fun newInstance(selectedDish: SelectedDish): CartAddBottomSheet {
            return CartAddBottomSheet().apply {
                this.selectedDish = selectedDish
            }
        }
    }
}

