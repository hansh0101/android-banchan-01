package co.kr.woowahan_banchan.presentation.ui.cart

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkManager
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.FragmentCartBinding
import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity
import co.kr.woowahan_banchan.presentation.adapter.CartAdapter
import co.kr.woowahan_banchan.presentation.decoration.VerticalItemDecoration
import co.kr.woowahan_banchan.presentation.notification.AlarmReceiver
import co.kr.woowahan_banchan.presentation.ui.base.BaseFragment
import co.kr.woowahan_banchan.presentation.ui.cart.history.HistoryFragment
import co.kr.woowahan_banchan.presentation.ui.order.orderdetail.OrderDetailFragment
import co.kr.woowahan_banchan.presentation.ui.productdetail.ProductDetailActivity
import co.kr.woowahan_banchan.presentation.ui.widget.ErrorDialog
import co.kr.woowahan_banchan.presentation.viewmodel.UiEvent
import co.kr.woowahan_banchan.presentation.viewmodel.UiState
import co.kr.woowahan_banchan.presentation.viewmodel.cart.CartViewModel
import co.kr.woowahan_banchan.util.dpToPx
import co.kr.woowahan_banchan.util.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_cart

    private val viewModel by viewModels<CartViewModel>()

    private val cartAdapter by lazy {
        CartAdapter(
            onHistoryItemClick = { title, hash ->
                startDetailActivity(title, hash)
            },
            onOrderBtnClick = { cartItems ->
                viewModel.orderStart(cartItems)
            },
            onCheckBtnClick = { cartItems ->
                viewModel.setSelectedAll(cartItems)
            },
            onFullRecentlyBtnClick = {
                parentFragmentManager.commit {
                    replace(R.id.fcv_cart, HistoryFragment())
                    addToBackStack("cart")
                }
            }
        )
    }

    private val workManager by lazy {
        WorkManager.getInstance(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        initToolbar()
        initView()
        observeData()
        setListener()
    }

    override fun onStop() {
        if (!viewModel.isOrdered) {
            viewModel.updateCartItems(cartAdapter.getCartItems(), workManager)
        }
        super.onStop()
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
        viewModel.cartItems
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is UiState.Init -> {}
                    is UiState.Success -> {
                        cartAdapter.updateCartItems(it.data)
                        binding.rvCart.scrollToPosition(0)
                        viewModel.setSelectedAll(it.data)
                    }
                    is UiState.Error -> {}
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.cartEvent
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                if (it is UiEvent.Error) {
                    showErrorDialogForCart(it)
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.historyItems.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is UiState.Init -> {}
                    is UiState.Success -> {
                        cartAdapter.updateHistoryItems(it.data)
                    }
                    is UiState.Error -> {
                        requireContext().shortToast(it.message)
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.orderId
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is UiState.Init -> {}
                    is UiState.Success -> {
                        val alarmManager =
                            requireContext().getSystemService(ALARM_SERVICE) as AlarmManager
                        val intent = Intent(AlarmReceiver.getIntent(requireContext(), it.data))
                        val pendingIntent = PendingIntent.getBroadcast(
                            requireContext(),
                            Date().time.toInt(),
                            intent,
                            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                        )
                        alarmManager.set(
                            AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            SystemClock.elapsedRealtime() + 1000 * 10,
                            pendingIntent
                        )

                        parentFragmentManager.commit {
                            replace(
                                R.id.fcv_cart,
                                OrderDetailFragment.newInstance(it.data),
                                OrderDetailFragment::class.java.simpleName
                            )
                        }
                    }
                    is UiState.Error -> {}
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.orderEvent
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                if (it is UiEvent.Error) {
                    showErrorDialogForOrder(it)
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setListener() {
        binding.ivCheckAll.setOnClickListener {
            viewModel.setSelectedAll(!viewModel.isSelectedAll.value)
            cartAdapter.setAllItemsSelected(viewModel.isSelectedAll.value)
        }

        binding.tvSelectedItemDelete.setOnClickListener {
            cartAdapter.deleteSelectedItems()
            viewModel.setSelectedAll(cartAdapter.getCartItems())
        }
    }

    private fun showErrorDialogForCart(event: UiEvent.Error) {
        ErrorDialog(
            requireContext(),
            event.error,
            { viewModel.reFetchCartItems() },
            {
                if (event.error is ErrorEntity.UnknownError) {
                    requireActivity().finish()
                }
            }
        ).show()
    }

    private fun showErrorDialogForOrder(event: UiEvent.Error) {
        ErrorDialog(requireContext(), event.error).show()
    }

    private fun startDetailActivity(title: String, hash: String) {
        startActivity(ProductDetailActivity.getIntent(requireContext(), title, hash))
    }
}