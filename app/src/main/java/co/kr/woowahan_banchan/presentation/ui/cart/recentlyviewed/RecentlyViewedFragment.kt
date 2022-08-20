package co.kr.woowahan_banchan.presentation.ui.cart.recentlyviewed

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.FragmentRecentlyViewedBinding
import co.kr.woowahan_banchan.presentation.adapter.RecentlyViewedAdapter
import co.kr.woowahan_banchan.presentation.decoration.GridItemDecoration
import co.kr.woowahan_banchan.presentation.ui.base.BaseFragment
import co.kr.woowahan_banchan.presentation.ui.productdetail.ProductDetailActivity
import co.kr.woowahan_banchan.presentation.ui.widget.CartAddBottomSheet
import co.kr.woowahan_banchan.presentation.viewmodel.UiState
import co.kr.woowahan_banchan.presentation.viewmodel.cart.RecentlyViewedViewModel
import co.kr.woowahan_banchan.util.dpToPx
import co.kr.woowahan_banchan.util.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RecentlyViewedFragment : BaseFragment<FragmentRecentlyViewedBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_recently_viewed

    private val viewModel by viewModels<RecentlyViewedViewModel>()
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
                    is UiState.Init -> {
                        showProgressBar()
                    }
                    is UiState.Success -> {
                        hideProgressBar()
                        recentlyViewedAdapter.submitList(it.data)
                    }
                    is UiState.Error -> {
                        hideProgressBar()
                        requireContext().shortToast(it.message)
                        parentFragmentManager.popBackStack()
                    }
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
}