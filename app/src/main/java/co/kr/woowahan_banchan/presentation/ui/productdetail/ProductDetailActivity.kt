package co.kr.woowahan_banchan.presentation.ui.productdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.ActivityProductDetailBinding
import co.kr.woowahan_banchan.domain.entity.detail.DishInfo
import co.kr.woowahan_banchan.presentation.adapter.ProductDetailViewPagerAdapter
import co.kr.woowahan_banchan.presentation.ui.base.BaseActivity
import co.kr.woowahan_banchan.presentation.ui.widget.CartAddDialog
import co.kr.woowahan_banchan.presentation.viewmodel.UiState
import co.kr.woowahan_banchan.presentation.viewmodel.productdetail.ProductDetailViewModel
import co.kr.woowahan_banchan.util.ImageLoader
import co.kr.woowahan_banchan.util.shortToast
import co.kr.woowahan_banchan.util.toPriceFormat
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class ProductDetailActivity : BaseActivity<ActivityProductDetailBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_product_detail

    private val viewModel by viewModels<ProductDetailViewModel>()
    private val viewPagerAdapter by lazy { ProductDetailViewPagerAdapter() }

    private lateinit var hash: String
    private lateinit var title: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hash = intent.getStringExtra("HASH") ?: error("Hash not delivered")
        title = intent.getStringExtra("TITLE") ?: error("Title not delivered")
        viewModel.fetchUiState(hash)
        initView()
        initOnClickListener()
        observeData()
    }

    private fun initView() {
        binding.vpProductDetail.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayoutIndicator, binding.vpProductDetail) { _, _ -> }.attach()
    }

    private fun initOnClickListener() {
        binding.ivPlus.setOnClickListener {
            viewModel.updateAmount(isPlus = true)
        }
        binding.ivMinus.setOnClickListener {
            viewModel.updateAmount(isPlus = false)
        }
        binding.btnOrder.setOnClickListener {
            viewModel.addToCart(hash, title)
        }
    }

    private fun observeData() {
        viewModel.dishInfo
            .flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is UiState.Init -> {
                        showProgressBar()
                    }
                    is UiState.Success -> {
                        Timber.tag("dishInfo").i(it.toString())
                        hideProgressBar()
                        showUi(it.data)
                    }
                    is UiState.Error -> {
                        hideProgressBar()
                        shortToast(it.message)
                        finish()
                    }
                }
            }.launchIn(lifecycleScope)

        viewModel.amount
            .flowWithLifecycle(lifecycle)
            .onEach {
                binding.tvAmountValue.text = it.toPriceFormat()
                if (viewModel.dishInfo.value is UiState.Success) {
                    val price =
                        (viewModel.dishInfo.value as UiState.Success).data.prices.last()
                    showTotalPrice(it, price)
                }
            }.launchIn(lifecycleScope)

        viewModel.isAddSuccess
            .flowWithLifecycle(lifecycle)
            .onEach { success ->
                when (success) {
                    true -> {
                        CartAddDialog(this).show()
                    }
                    false -> shortToast("실패")
                }
            }.launchIn(lifecycleScope)
    }

    private fun showUi(dishInfo: DishInfo) {
        viewPagerAdapter.updateItems(dishInfo.thumbnailUrls)
        binding.tvTitle.text = title
        binding.tvDescription.text = dishInfo.productDescription
        binding.tvSalePercent.isVisible = dishInfo.discount != 0
        binding.tvSalePercent.text = "${dishInfo.discount}%"
        binding.tvSalePrice.text = dishInfo.prices.last().toPriceFormat() + "원"
        binding.tvOriginalPrice.isVisible = dishInfo.prices.size != 1
        binding.tvOriginalPrice.text = dishInfo.prices.first().toPriceFormat() + "원"
        binding.tvPointValue.text = dishInfo.point.toPriceFormat() + "원"
        binding.tvDeliveryInfoValue.text = dishInfo.deliveryInfo
        binding.tvDeliveryFeeValue.text = dishInfo.deliveryFeeInfo
        showTotalPrice(viewModel.amount.value, dishInfo.prices.last())
        if (binding.layoutDetailImage.childCount == 0) addDetailImages(dishInfo.detailImageUrls)
    }

    private fun showTotalPrice(amount: Int, price: Int) {
        val totalPrice = amount * price
        binding.tvTotalPriceValue.text = totalPrice.toPriceFormat() + "원"
    }

    private fun addDetailImages(imageUrls: List<String>) {
        for (imageUrl in imageUrls) {
            val imageView = ImageView(this)
            binding.layoutDetailImage.addView(imageView)
            with(imageView) {
                this.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                this.adjustViewBounds = true
                ImageLoader.loadImage(imageUrl) {
                    if (it != null) {
                        this.setImageBitmap(it)
                    }
                }
            }
        }
    }

    private fun showProgressBar() {
        binding.progressBar.isVisible = true
        binding.svProductDetail.isVisible = false
    }

    private fun hideProgressBar() {
        binding.progressBar.isVisible = false
        binding.svProductDetail.isVisible = true
    }

    companion object {
        fun getIntent(context: Context, title: String, hash: String): Intent {
            return Intent(context, ProductDetailActivity::class.java).apply {
                putExtra("TITLE", title)
                putExtra("HASH", hash)
            }
        }
    }
}