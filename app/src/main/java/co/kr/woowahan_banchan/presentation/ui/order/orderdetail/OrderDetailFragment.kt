package co.kr.woowahan_banchan.presentation.ui.order.orderdetail

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.FragmentOrderDetailBinding
import co.kr.woowahan_banchan.databinding.ItemOrderDetailBinding
import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderItem
import co.kr.woowahan_banchan.presentation.ui.base.BaseFragment
import co.kr.woowahan_banchan.presentation.ui.order.OrderActivity
import co.kr.woowahan_banchan.presentation.ui.productdetail.ProductDetailActivity
import co.kr.woowahan_banchan.presentation.viewmodel.UiState
import co.kr.woowahan_banchan.presentation.viewmodel.order.OrderDetailViewModel
import co.kr.woowahan_banchan.util.ImageLoader
import co.kr.woowahan_banchan.util.longArgs
import co.kr.woowahan_banchan.util.shortToast
import co.kr.woowahan_banchan.util.toPriceFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class OrderDetailFragment : BaseFragment<FragmentOrderDetailBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_order_detail

    private val viewModel by viewModels<OrderDetailViewModel>()
    private val orderId by longArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchOrderItems(orderId)
        initView()
        observeData()
    }

    private fun initView() {
        (requireActivity() as? OrderActivity)?.setToolbar(OrderActivity.Companion.FragmentType.ORDER_DETAIL)
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
                        requireContext().shortToast(it.message)
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun showUi(orderItems: List<OrderItem>) {
        val menuTotalPrice = orderItems.sumOf { it.price * it.amount }
        binding.tvMenuAmountValue.text = "총 ${orderItems.size}개"
        binding.tvMenuPriceValue.text = menuTotalPrice.toPriceFormat() + "원"
        binding.tvMenuDeliveryFeeValue.text =
            if (menuTotalPrice >= 40000) "0원"
            else 2500.toPriceFormat() + "원"
        binding.tvPriceValue.text =
            if (menuTotalPrice >= 40000) (menuTotalPrice).toPriceFormat() + "원"
            else (menuTotalPrice + 2500).toPriceFormat() + "원"
        if (binding.layoutMenu.childCount == 0) addMenuItemView(orderItems)
    }

    private fun addMenuItemView(orderItems: List<OrderItem>) {
        orderItems.forEach { orderItem ->
            val itemBinding = ItemOrderDetailBinding.inflate(layoutInflater, null, false)
            ImageLoader.loadImage(orderItem.thumbnailUrl) {
                if (it != null) {
                    itemBinding.ivImage.setImageBitmap(it)
                }
            }
            itemBinding.tvTitle.text = orderItem.title
            itemBinding.tvAmount.text = "${orderItem.amount}개"
            itemBinding.tvPrice.text = (orderItem.price * orderItem.amount).toPriceFormat() + "원"
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

    companion object {
        fun newInstance(orderId: Long): OrderDetailFragment {
            return OrderDetailFragment().apply {
                arguments = Bundle().apply {
                    putLong("orderId", orderId)
                }
            }
        }
    }
}