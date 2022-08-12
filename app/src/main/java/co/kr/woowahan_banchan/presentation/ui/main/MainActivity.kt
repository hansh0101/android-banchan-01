package co.kr.woowahan_banchan.presentation.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.ActivityMainBinding
import co.kr.woowahan_banchan.presentation.adapter.ViewPagerAdapter
import co.kr.woowahan_banchan.presentation.ui.base.BaseActivity
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_main

    private val tabTitleArray = arrayOf(
        "기획전",
        "든든한 메인요리",
        "뜨끈한 국물요리",
        "정갈한 밑반찬"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()
        initView()
    }

    private fun initToolbar(){
        setSupportActionBar(binding.tbToolbar)
    }

    private fun initView(){
        with(binding){
            vpFragmentPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

            TabLayoutMediator(layoutFragmentTab,vpFragmentPager){ tab, position ->
                tab.text = tabTitleArray[position]
            }.attach()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_app_bar,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_cart -> {
                //move to cart
            }
            R.id.action_history -> {
                //move to history
            }
        }
        return super.onOptionsItemSelected(item)
    }
}