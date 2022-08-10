package co.kr.woowahan_banchan.presentation

import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.ActivityMainBinding
import co.kr.woowahan_banchan.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_main
}