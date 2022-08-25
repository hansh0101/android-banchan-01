package co.kr.woowahan_banchan.presentation.ui.cart.history

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.FragmentHistoryBinding
import co.kr.woowahan_banchan.presentation.adapter.RecentlyViewedAdapter
import co.kr.woowahan_banchan.presentation.decoration.GridItemDecoration
import co.kr.woowahan_banchan.presentation.ui.base.BaseFragment
import co.kr.woowahan_banchan.presentation.ui.productdetail.ProductDetailActivity
import co.kr.woowahan_banchan.presentation.ui.widget.CartAddBottomSheet
import co.kr.woowahan_banchan.presentation.ui.widget.ErrorDialog
import co.kr.woowahan_banchan.presentation.viewmodel.UiEvents
import co.kr.woowahan_banchan.presentation.viewmodel.UiStates
import co.kr.woowahan_banchan.presentation.viewmodel.cart.HistoryViewModel
import co.kr.woowahan_banchan.util.dpToPx
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_history

    private val viewModel by viewModels<HistoryViewModel>()
    private val recentlyViewedAdapter by lazy {
        RecentlyViewedAdapter({
            startActivity(
                ProductDetailActivity.getIntent(
                    requireContext(),
                    it.title,
                    it.detailHash
                )
            )
        }, {
            CartAddBottomSheet.newInstance(it.toSelectedDish()).show(parentFragmentManager, null)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
        observeData()
    }

    private fun initView() {
        initToolbar()
        binding.rvRecentlyViewed.adapter = recentlyViewedAdapter
        binding.rvRecentlyViewed.addItemDecoration(
            GridItemDecoration(
                16.dpToPx(),
                32.dpToPx(),
                8.dpToPx()
            )
        )
    }

    private fun initData() {
        viewModel.fetchHistoryItems()
    }

    private fun observeData() {
        viewModel.historyItems
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is UiStates.Init -> {
                        showProgressBar()
                    }
                    is UiStates.Success -> {
                        hideProgressBar()
                        recentlyViewedAdapter.submitList(it.data)
                    }
                    is UiStates.Error -> {
                        hideProgressBar()
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.historyItemsEvent
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                if (it is UiEvents.Error) {
                    showErrorDialog(it)
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initToolbar() {
        requireActivity().title = "Recently viewed products"
    }

    private fun showProgressBar() {
        binding.rvRecentlyViewed.isVisible = false
        binding.progressBar.isVisible = true
    }

    private fun hideProgressBar() {
        binding.rvRecentlyViewed.isVisible = true
        binding.progressBar.isVisible = false
    }

    private fun showErrorDialog(event: UiEvents.Error) {
        ErrorDialog(
            requireContext(),
            event.error,
            { viewModel.reFetchHistoryItems() },
            { parentFragmentManager.popBackStack() }
        ).show()
    }
}