package co.kr.woowahan_banchan.presentation.ui.order.orderlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.FragmentOrderListBinding
import co.kr.woowahan_banchan.presentation.adapter.OrderListAdapter
import co.kr.woowahan_banchan.presentation.ui.base.BaseFragment
import co.kr.woowahan_banchan.presentation.ui.order.OrderActivity

class OrderListFragment : BaseFragment<FragmentOrderListBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_order_list

    private val orderListAdapter by lazy { OrderListAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        (requireActivity() as? OrderActivity)?.setToolbar(OrderActivity.Companion.FragmentType.ORDER_LIST)
        binding.rvOrderList.adapter = orderListAdapter
    }
}