package co.kr.woowahan_banchan.presentation.ui.main

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat.animate
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.ActivityMainBinding
import co.kr.woowahan_banchan.presentation.adapter.ViewPagerAdapter
import co.kr.woowahan_banchan.presentation.ui.base.BaseActivity
import co.kr.woowahan_banchan.presentation.ui.cart.CartActivity
import co.kr.woowahan_banchan.presentation.ui.order.OrderActivity
import co.kr.woowahan_banchan.presentation.viewmodel.MainViewModel
import co.kr.woowahan_banchan.util.dpToPx
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_main

    private val viewModel by viewModels<MainViewModel>()

    private val tabTitleArray = arrayOf(
        "기획전",
        "든든한 메인요리",
        "뜨끈한 국물요리",
        "정갈한 밑반찬"
    )

    private val badge by lazy {
        BadgeDrawable.create(this).also { bd ->
            bd.backgroundColor = resources.getColor(R.color.grayscale_000000, theme)
            bd.horizontalOffset = 5.dpToPx()
            bd.verticalOffset = 5.dpToPx()
            bd.maxCharacterCount = 3
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        exitSplashScreen()

        super.onCreate(savedInstanceState)

        initToolbar()
        initView()
        observeData()

        viewModel.getCartItemCount()
        viewModel.fetchLatestOrderTime()
    }

    private fun exitSplashScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                lifecycleScope.launch {
                    splashScreenView.iconView?.let {
                        animate(it).scaleX(0.7f).scaleY(0.7f).setDuration(300L).start()
                        delay(300)
                        animate(it).scaleX(2f).scaleY(2f).setDuration(300L).start()
                        delay(300)
                    }
                }
                ObjectAnimator.ofFloat(
                    splashScreenView.iconView,
                    View.TRANSLATION_X,
                    0f,
                    -splashScreenView.width.toFloat()
                ).apply {
                    duration = 1000L
                    interpolator = AnticipateOvershootInterpolator()
                    doOnEnd { splashScreenView.remove() }
                }.start()
            }
        }
    }

    private fun initToolbar() {
        setSupportActionBar(binding.tbToolbar)
    }

    private fun initView() {
        with(binding) {
            vpFragmentPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

            TabLayoutMediator(layoutFragmentTab, vpFragmentPager) { tab, position ->
                tab.text = tabTitleArray[position]
            }.attach()
        }
    }

    private fun observeData() {
        viewModel.cartCount.flowWithLifecycle(this.lifecycle)
            .onEach {
                badge.number = it
                badge.isVisible = it > 0
            }.launchIn(lifecycleScope)

        viewModel.isOrderCompleted
            .flowWithLifecycle(this.lifecycle)
            .onEach {
                invalidateOptionsMenu()
            }.launchIn(lifecycleScope)
    }

    @ExperimentalBadgeUtils
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_app_bar, menu)
        BadgeUtils.attachBadgeDrawable(badge, binding.tbToolbar, R.id.action_cart)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_cart -> {
                startActivity(CartActivity.getIntent(this))
            }
            R.id.action_history -> {
                startActivity(OrderActivity.getIntent(this))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (viewModel.isOrderCompleted.value) {
            menu?.let {
                it.getItem(1).icon = ContextCompat.getDrawable(this, R.drawable.ic_user)
            }
        } else {
            menu?.let {
                it.getItem(1).icon = ContextCompat.getDrawable(this, R.drawable.ic_user_badge)
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }
}