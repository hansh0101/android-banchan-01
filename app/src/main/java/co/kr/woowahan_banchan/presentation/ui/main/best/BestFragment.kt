package co.kr.woowahan_banchan.presentation.ui.main.best

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.FragmentBestBinding
import co.kr.woowahan_banchan.presentation.adapter.BestItemAdapter
import co.kr.woowahan_banchan.presentation.decoration.VerticalItemDecoration
import co.kr.woowahan_banchan.presentation.ui.base.BaseFragment
import co.kr.woowahan_banchan.presentation.ui.productdetail.ProductDetailActivity
import co.kr.woowahan_banchan.presentation.ui.widget.CartAddBottomSheet
import co.kr.woowahan_banchan.presentation.ui.widget.ErrorDialog
import co.kr.woowahan_banchan.presentation.viewmodel.UiEvents
import co.kr.woowahan_banchan.presentation.viewmodel.UiStates
import co.kr.woowahan_banchan.presentation.viewmodel.main.BestViewModel
import co.kr.woowahan_banchan.util.dpToPx
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class BestFragment : BaseFragment<FragmentBestBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_best

    private val viewModel by viewModels<BestViewModel>()
    private val bestAdapter = BestItemAdapter(
        moveToDetail = { title, hash ->
            startDetailActivity(title, hash)
        },
        openBottomSheet = {
            CartAddBottomSheet.newInstance(it.toSelectedDish()).show(parentFragmentManager, null)
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
    }

    private fun initView() {
        binding.rvBests.adapter = bestAdapter
        binding.rvBests.layoutManager = LinearLayoutManager(requireContext())
        if (binding.rvBests.itemDecorationCount == 0)
            binding.rvBests.addItemDecoration(VerticalItemDecoration(24.dpToPx(), 0.dpToPx()))
    }

    private fun observeData() {
        viewModel.bestItems
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                binding.pbLoading.isVisible = it is UiStates.Init
                when (it) {
                    is UiStates.Init -> {}
                    is UiStates.Success -> {
                        bestAdapter.submitList(it.data.toMutableList())
                    }
                    is UiStates.Error -> {}
                }
            }.launchIn(lifecycleScope)

        viewModel.bestItemsEvent
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                if (it is UiEvents.Error) {
                    showErrorDialog(it)
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun startDetailActivity(title: String, hash: String) {
        startActivity(ProductDetailActivity.getIntent(requireContext(), title, hash))
    }

    private fun showErrorDialog(event: UiEvents.Error) {
        ErrorDialog(requireContext(), event.error, { viewModel.reFetchBests() }).show()
    }
}