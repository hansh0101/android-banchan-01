package co.kr.woowahan_banchan.presentation.ui.order

import android.content.Context
import android.content.Intent
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
        initToolbar()
        supportFragmentManager.commit {
            add<OrderListFragment>(R.id.fcv_order)
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

    companion object {
        fun getIntent(context: Context): Intent =
            Intent(context, OrderActivity::class.java)
    }
}