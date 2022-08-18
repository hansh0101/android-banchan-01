package co.kr.woowahan_banchan.presentation.ui.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.FragmentCartBinding
import co.kr.woowahan_banchan.domain.entity.cart.CartItem
import co.kr.woowahan_banchan.domain.entity.history.HistoryItem
import co.kr.woowahan_banchan.presentation.adapter.CartAdapter
import co.kr.woowahan_banchan.presentation.decoration.VerticalItemDecoration
import co.kr.woowahan_banchan.presentation.ui.base.BaseFragment
import co.kr.woowahan_banchan.presentation.ui.cart.recentlyviewed.RecentlyViewedFragment
import co.kr.woowahan_banchan.presentation.ui.order.orderdetail.OrderDetailFragment
import co.kr.woowahan_banchan.presentation.ui.productdetail.ProductDetailActivity
import co.kr.woowahan_banchan.presentation.viewmodel.UiState
import co.kr.woowahan_banchan.presentation.viewmodel.cart.CartViewModel
import co.kr.woowahan_banchan.util.dpToPx
import co.kr.woowahan_banchan.util.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_cart

    private val viewModel by viewModels<CartViewModel>()

    private val cartAdapter by lazy {
        CartAdapter(object : CartAdapter.OnCartClickListener {
            override fun onHistoryItemClick(historyItem: HistoryItem) {
                startDetailActivity(historyItem.title, historyItem.detailHash)
            }

            override fun onOrderBtnClick(cartItems: List<CartItem>) {
                viewModel.orderStart(cartItems)
            }

            override fun onCheckBtnClick(cartItems: List<CartItem>) {
                viewModel.setSelectedAll(cartItems)
            }

            override fun onFullRecentlyBtnClick() {
                parentFragmentManager.commit {
                    replace(R.id.fcv_cart, RecentlyViewedFragment())
                    addToBackStack("cart")
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initView()
        observeData()
        setListener()

        binding.viewModel = viewModel
        viewModel.getCartItems()
        viewModel.getRecentlyViewed()
    }

    private fun initToolbar() {
        requireActivity().title = "Cart"
    }

    private fun initView() {
        binding.rvCart.adapter = cartAdapter
        binding.rvCart.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCart.itemAnimator = null
        binding.rvCart.addItemDecoration(VerticalItemDecoration(2.dpToPx(), 0))
    }

    private fun observeData() {
        viewModel.cartItems.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is UiState.Success -> {
                        cartAdapter.updateCartItems(it.data)
                        binding.rvCart.scrollToPosition(0)
                        viewModel.setSelectedAll(it.data)
                    }
                    is UiState.Error -> {
                        requireContext().shortToast(it.message)
                    }
                    else -> {

                    }
                }
            }.launchIn(lifecycleScope)

        viewModel.historyItems.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is UiState.Success -> {
                        cartAdapter.updateHistoryItems(it.data)
                    }
                    is UiState.Error -> {
                        requireContext().shortToast(it.message)
                    }
                    else -> {

                    }
                }
            }.launchIn(lifecycleScope)

        viewModel.orderId.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is UiState.Success -> {
                        parentFragmentManager.commit {
                            replace(R.id.fcv_cart, OrderDetailFragment.newInstance(it.data))
                        }
                    }
                    is UiState.Error -> {
                        requireContext().shortToast(it.message)
                        requireActivity().finish()
                    }
                }
            }.launchIn(lifecycleScope)
    }

    private fun setListener() {
        binding.ivCheckAll.setOnClickListener {
            viewModel.setSelectedAll(!viewModel.isSelectedAll.value)
            cartAdapter.setAllItemsSelected(viewModel.isSelectedAll.value)
        }
    }

    private fun startDetailActivity(title: String, hash: String) {
        startActivity(ProductDetailActivity.getIntent(requireContext(), title, hash))
    }

    override fun onStop() {
        if (!viewModel.isOrdered) {
            viewModel.updateCartItems(cartAdapter.getCartItems())
        }
        super.onStop()
    }
}