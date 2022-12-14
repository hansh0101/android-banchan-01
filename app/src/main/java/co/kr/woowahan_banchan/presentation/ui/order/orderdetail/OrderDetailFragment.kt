package co.kr.woowahan_banchan.presentation.ui.order.orderdetail

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.FragmentOrderDetailBinding
import co.kr.woowahan_banchan.databinding.ItemOrderDetailBinding
import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderItem
import co.kr.woowahan_banchan.presentation.ui.base.BaseFragment
import co.kr.woowahan_banchan.presentation.ui.productdetail.ProductDetailActivity
import co.kr.woowahan_banchan.presentation.ui.widget.ErrorDialog
import co.kr.woowahan_banchan.presentation.viewmodel.UiEvent
import co.kr.woowahan_banchan.presentation.viewmodel.UiState
import co.kr.woowahan_banchan.presentation.viewmodel.order.OrderDetailViewModel
import co.kr.woowahan_banchan.util.ImageLoader
import co.kr.woowahan_banchan.util.calculateDiffToSecond
import co.kr.woowahan_banchan.util.longArgs
import co.kr.woowahan_banchan.util.toPriceFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*

@AndroidEntryPoint
class OrderDetailFragment : BaseFragment<FragmentOrderDetailBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_order_detail

    private val viewModel by viewModels<OrderDetailViewModel>()
    private val orderId by longArgs()
    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_order, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.menu_refresh -> {
                    if (viewModel.orderTime.value is UiState.Success<Long>) {
                        showDeliveryInfo((viewModel.orderTime.value as UiState.Success<Long>).data)
                    }
                    true
                }
                else -> false
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchOrderItems(orderId)
        initToolbar()
        observeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().removeMenuProvider(menuProvider)
    }

    private fun initToolbar() {
        requireActivity().title = ""
        val menuHost = requireActivity()
        menuHost.addMenuProvider(menuProvider)
    }

    private fun observeData() {
        viewModel.orderItems
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
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.orderTime
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is UiState.Init -> {}
                    is UiState.Success -> {
                        showDeliveryInfo(it.data)
                    }
                    is UiState.Error -> {}
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.orderItemsEvent
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                if (it is UiEvent.Error) {
                    showErrorDialog(it)
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.orderTimeEvent
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                if (it is UiEvent.Error) {
                    showErrorDialog(it)
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun showUi(orderItems: List<OrderItem>) {
        val menuTotalPrice = orderItems.sumOf { it.price * it.amount }
        binding.tvMenuAmountValue.text = "??? ${orderItems.size}???"
        binding.tvMenuPriceValue.text = menuTotalPrice.toPriceFormat() + "???"
        binding.tvMenuDeliveryFeeValue.text =
            if (menuTotalPrice >= 40000) "0???"
            else 2500.toPriceFormat() + "???"
        binding.tvPriceValue.text =
            if (menuTotalPrice >= 40000) (menuTotalPrice).toPriceFormat() + "???"
            else (menuTotalPrice + 2500).toPriceFormat() + "???"
        if (binding.layoutMenu.childCount == 0) addMenuItemView(orderItems)
    }

    private fun showDeliveryInfo(time: Long) {
        if (Date().time.calculateDiffToSecond(time) >= 10) {
            binding.tvTitleOrderInfo.text = "????????? ?????????????????????."
            binding.layoutDeliveryWaiting.isVisible = false
        } else {
            binding.tvTitleOrderInfo.text = "????????? ?????????????????????."
            binding.tvDeliveryWaitingValue.text = "${10 - Date().time.calculateDiffToSecond(time)}???"
            binding.layoutDeliveryWaiting.isVisible = true
        }
    }

    private fun addMenuItemView(orderItems: List<OrderItem>) {
        orderItems.forEach { orderItem ->
            val itemBinding = ItemOrderDetailBinding.inflate(layoutInflater, null, false)
            ImageLoader(itemBinding.ivImage, requireContext())
                .setPlaceHolder(R.mipmap.ic_launcher)
                .setErrorImage(R.mipmap.ic_launcher)
                .loadImage(orderItem.thumbnailUrl)

            itemBinding.tvTitle.text = orderItem.title
            itemBinding.tvAmount.text = "${orderItem.amount}???"
            itemBinding.tvPrice.text = (orderItem.price * orderItem.amount).toPriceFormat() + "???"
            itemBinding.root.setOnClickListener {
                startActivity(
                    ProductDetailActivity.getIntent(
                        requireContext(),
                        orderItem.title,
                        orderItem.hash
                    )
                )
            }

            binding.layoutMenu.addView(itemBinding.root)
        }
    }

    private fun showProgressBar() {
        binding.svOrderDetail.isVisible = false
        binding.progressBar.isVisible = true
    }

    private fun hideProgressBar() {
        binding.svOrderDetail.isVisible = true
        binding.progressBar.isVisible = false
    }

    private fun showErrorDialog(event: UiEvent.Error) {
        ErrorDialog(
            requireContext(),
            event.error,
            { viewModel.fetchOrderItems(orderId) }
        ).show()
    }

    companion object {
        var selectedOrderId : Long? = null
        fun newInstance(orderId: Long): OrderDetailFragment {
            selectedOrderId = orderId
            return OrderDetailFragment().apply {
                arguments = Bundle().apply {
                    putLong("orderId", orderId)
                }
            }
        }
    }
}