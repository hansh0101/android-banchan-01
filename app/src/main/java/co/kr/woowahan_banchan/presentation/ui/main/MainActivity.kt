package co.kr.woowahan_banchan.presentation.ui.main

import android.os.Bundle
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

        initView()
    }

    private fun initView(){
        with(binding){
            vpFragmentPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

            TabLayoutMediator(layoutFragmentTab,vpFragmentPager){ tab, position ->
                tab.text = tabTitleArray[position]
            }.attach()
        }
    }
}