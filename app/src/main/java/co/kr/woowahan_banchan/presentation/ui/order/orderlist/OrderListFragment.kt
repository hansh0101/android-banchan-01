package co.kr.woowahan_banchan.presentation.ui.order.orderlist

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.FragmentOrderListBinding
import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderHistory
import co.kr.woowahan_banchan.presentation.adapter.OrderListAdapter
import co.kr.woowahan_banchan.presentation.ui.base.BaseFragment
import co.kr.woowahan_banchan.presentation.ui.order.OrderActivity
import co.kr.woowahan_banchan.presentation.viewmodel.UiState
import co.kr.woowahan_banchan.presentation.viewmodel.order.OrderListViewModel
import co.kr.woowahan_banchan.util.OrderListItemDecoration
import co.kr.woowahan_banchan.util.dpToPx
import co.kr.woowahan_banchan.util.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class OrderListFragment : BaseFragment<FragmentOrderListBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_order_list

    private val viewModel by viewModels<OrderListViewModel>()
    private val orderListAdapter by lazy { OrderListAdapter { requireContext().shortToast("TODO") } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchOrderHistories()
        initView()
        observeData()
    }

    private fun initView() {
        (requireActivity() as? OrderActivity)?.setToolbar(OrderActivity.Companion.FragmentType.ORDER_LIST)
        binding.rvOrderList.addItemDecoration(
            OrderListItemDecoration(
                5.dpToPx(),
                5.dpToPx(),
                1.dpToPx(),
                requireContext().resources.getColor(R.color.grayscale_dddddd, null)
            )
        )
        binding.rvOrderList.adapter = orderListAdapter
    }

    private fun observeData() {
        viewModel.orderHistories
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is UiState.Init -> {
                        showProgressBar()
                    }
                    is UiState.Success -> {
                        hideProgressBar()
                        showUi(it.data)
                    }
                    is UiState.Error -> {
                        hideProgressBar()
                        requireContext().shortToast(it.message)
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun showUi(orderHistories: List<OrderHistory>) {
        orderListAdapter.submitList(orderHistories)
    }

    private fun showProgressBar() {
        binding.progressBar.isVisible = true
        binding.rvOrderList.isVisible = false
    }

    private fun hideProgressBar() {
        binding.progressBar.isVisible = false
        binding.rvOrderList.isVisible = true
    }
}