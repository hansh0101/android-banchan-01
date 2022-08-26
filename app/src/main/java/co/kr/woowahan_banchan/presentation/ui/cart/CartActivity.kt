package co.kr.woowahan_banchan.presentation.ui.cart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.commit
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.ActivityCartBinding
import co.kr.woowahan_banchan.presentation.ui.base.BaseActivity
import co.kr.woowahan_banchan.presentation.ui.cart.history.HistoryFragment
import co.kr.woowahan_banchan.presentation.ui.order.orderdetail.OrderDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartActivity : BaseActivity<ActivityCartBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_cart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView(
            savedInstanceState?.getInt("backStackCount"),
            savedInstanceState?.getBoolean("orderDetail") ?: false
        )
    }

    private fun initView(backStackCount: Int?, orderDetail: Boolean) {
        initToolbar()
        while (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
        }
        supportFragmentManager.commit {
            if (orderDetail) {
                replace(
                    R.id.fcv_cart,
                    OrderDetailFragment.newInstance(OrderDetailFragment.selectedOrderId!!),
                    OrderDetailFragment::class.java.simpleName
                )
            } else {
                replace(R.id.fcv_cart, CartFragment())
                if (backStackCount != 0 && backStackCount != null) {
                    replace(R.id.fcv_cart, HistoryFragment())
                    addToBackStack("cart")
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("backStackCount", supportFragmentManager.backStackEntryCount)
        outState.putBoolean(
            "orderDetail",
            supportFragmentManager.findFragmentByTag(OrderDetailFragment::class.java.simpleName) != null
        )
    }

    private fun initToolbar() {
        setSupportActionBar(binding.tbToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (supportFragmentManager.backStackEntryCount == 0) {
                finish()
            } else {
                supportFragmentManager.popBackStack()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun getIntent(context: Context): Intent =
            Intent(context, CartActivity::class.java)
    }
}