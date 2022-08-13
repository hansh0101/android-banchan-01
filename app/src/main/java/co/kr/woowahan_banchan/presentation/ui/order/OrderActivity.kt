package co.kr.woowahan_banchan.presentation.ui.order

import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.ActivityOrderBinding
import co.kr.woowahan_banchan.presentation.ui.base.BaseActivity
import co.kr.woowahan_banchan.presentation.ui.order.orderlist.OrderListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderActivity : BaseActivity<ActivityOrderBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        supportFragmentManager.commit {
            add<OrderListFragment>(R.id.fcv_order)
        }
    }

    fun setToolbar(fragmentType: FragmentType) {
        when (fragmentType) {
            FragmentType.ORDER_LIST -> {
                binding.tbToolbar.title = "OrderList"
                binding.tbToolbar.setNavigationIcon(R.drawable.ic_arrow_left)
                binding.tbToolbar.setNavigationOnClickListener { finish() }
            }
        }
    }

    companion object {
        enum class FragmentType {
            ORDER_LIST
        }
    }
}