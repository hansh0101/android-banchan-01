package co.kr.woowahan_banchan.presentation.ui.order

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.commit
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.ActivityOrderBinding
import co.kr.woowahan_banchan.presentation.ui.base.BaseActivity
import co.kr.woowahan_banchan.presentation.ui.order.orderdetail.OrderDetailFragment
import co.kr.woowahan_banchan.presentation.ui.order.orderlist.OrderListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderActivity : BaseActivity<ActivityOrderBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView(savedInstanceState?.getInt("backStackCount"))
    }

    private fun initView(backStackCount : Int?) {
        initToolbar()
        while (supportFragmentManager.backStackEntryCount > 0){
            supportFragmentManager.popBackStackImmediate()
        }
        supportFragmentManager.commit {
            replace(R.id.fcv_order,OrderListFragment())
            if (backStackCount != 0 && backStackCount != null){
                replace(R.id.fcv_order, OrderDetailFragment.newInstance(OrderDetailFragment.selectedOrderId!!))
                addToBackStack("orderList")
            }
        }
    }

    private fun initToolbar() {
        setSupportActionBar(binding.tbToolbar)
        binding.tbToolbar.setNavigationIcon(R.drawable.ic_arrow_left)
        binding.tbToolbar.setNavigationOnClickListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                finish()
            } else {
                supportFragmentManager.popBackStack()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("backStackCount",supportFragmentManager.backStackEntryCount)
    }

    companion object {
        fun getIntent(context: Context): Intent =
            Intent(context, OrderActivity::class.java)
    }
}