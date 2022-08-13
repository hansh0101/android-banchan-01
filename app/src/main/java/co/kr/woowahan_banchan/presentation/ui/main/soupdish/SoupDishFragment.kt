package co.kr.woowahan_banchan.presentation.ui.main.soupdish

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.FragmentSoupDishBinding
import co.kr.woowahan_banchan.domain.repository.Source
import co.kr.woowahan_banchan.presentation.adapter.DishAdapter
import co.kr.woowahan_banchan.presentation.adapter.FilterSpinnerAdapter
import co.kr.woowahan_banchan.presentation.decoration.GridItemDecoration
import co.kr.woowahan_banchan.presentation.ui.base.BaseFragment
import co.kr.woowahan_banchan.presentation.ui.productdetail.ProductDetailActivity
import co.kr.woowahan_banchan.presentation.viewmodel.UiState
import co.kr.woowahan_banchan.presentation.viewmodel.main.SoupDishViewModel
import co.kr.woowahan_banchan.util.dpToPx
import co.kr.woowahan_banchan.util.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SoupDishFragment : BaseFragment<FragmentSoupDishBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_soup_dish

    private val viewModel by viewModels<SoupDishViewModel>()

    private val dishAdapter by lazy {
        DishAdapter { title, hash ->
            startDetailActivity(
                title,
                hash
            )
        }
    }

    private val spinnerItems = listOf("기본 정렬순", "금액 높은순", "금액 낮은순", "할인율순")

    private val filterAdapter by lazy { FilterSpinnerAdapter(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observeData()
        setListener()

        viewModel.getDishes(Source.SOUP)
    }

    private fun initView() {
        binding.rvSoupdishes.adapter = dishAdapter
        binding.rvSoupdishes.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvSoupdishes.addItemDecoration(
            GridItemDecoration(30.dpToPx(),16.dpToPx())
        )
        binding.rvSoupdishes.itemAnimator = null

        binding.spFilter.adapter = filterAdapter
        filterAdapter.submitList(spinnerItems, 0)
    }

    private fun observeData() {
        viewModel.soupDishes.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is UiState.Init -> {
                        binding.pbLoading.visibility = View.VISIBLE
                    }
                    is UiState.Success -> {
                        viewModel.setDefaultMainDishes(it.data)
                        viewModel.setSortedDishes(binding.spFilter.selectedItemPosition)
                        binding.pbLoading.visibility = View.GONE
                    }
                    is UiState.Error -> {
                        binding.pbLoading.visibility = View.GONE
                        requireContext().shortToast(it.message)
                    }
                }
            }.launchIn(lifecycleScope)

        viewModel.sortedDishes.observe(viewLifecycleOwner) {
            dishAdapter.submitList(it.toMutableList())
        }
    }

    private fun setListener() {
        binding.spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setSortedDishes(position)
                filterAdapter.submitList(
                    spinnerItems, position
                )
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    private fun startDetailActivity(title: String, hash: String) {
        startActivity(ProductDetailActivity.getIntent(requireContext(), title, hash))
    }
}