package co.kr.woowahan_banchan.presentation.ui.cart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.add
import androidx.fragment.app.commit
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.ActivityCartBinding
import co.kr.woowahan_banchan.presentation.ui.base.BaseActivity
import co.kr.woowahan_banchan.presentation.ui.cart.recentlyviewed.RecentlyViewedFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CartActivity : BaseActivity<ActivityCartBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_cart

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
            replace(R.id.fcv_cart,CartFragment())
            if (backStackCount != 0 && backStackCount != null){
                replace(R.id.fcv_cart,RecentlyViewedFragment())
                addToBackStack("cart")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("backStackCount",supportFragmentManager.backStackEntryCount)
    }

    private fun initToolbar() {
        setSupportActionBar(binding.tbToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
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