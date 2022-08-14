package co.kr.woowahan_banchan.presentation.ui.main.maindish

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.FragmentMainDishBinding
import co.kr.woowahan_banchan.domain.entity.dish.Dish
import co.kr.woowahan_banchan.domain.repository.Source
import co.kr.woowahan_banchan.presentation.adapter.DishAdapter
import co.kr.woowahan_banchan.presentation.adapter.FilterSpinnerAdapter
import co.kr.woowahan_banchan.presentation.decoration.GridItemDecoration
import co.kr.woowahan_banchan.presentation.decoration.VerticalItemDecoration
import co.kr.woowahan_banchan.presentation.ui.base.BaseFragment
import co.kr.woowahan_banchan.presentation.ui.productdetail.ProductDetailActivity
import co.kr.woowahan_banchan.presentation.ui.widget.CartAddBottomSheet
import co.kr.woowahan_banchan.presentation.viewmodel.UiState
import co.kr.woowahan_banchan.presentation.viewmodel.main.MainDishViewModel
import co.kr.woowahan_banchan.util.dpToPx
import co.kr.woowahan_banchan.util.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainDishFragment : BaseFragment<FragmentMainDishBinding>() {

    override val layoutRes: Int
        get() = R.layout.fragment_main_dish

    private val viewModel by viewModels<MainDishViewModel>()

    private val dishAdapter by lazy {
        DishAdapter(object : DishAdapter.DishClickListener {
            override fun moveToDetail(title: String, hash: String) {
                startDetailActivity(title, hash)
            }

            override fun openBottomSheet(dish: Dish) {
                CartAddBottomSheet.newInstance(dish).show(parentFragmentManager, null)
            }
        })
    }

    private val spinnerItems = listOf("기본 정렬순", "금액 높은순", "금액 낮은순", "할인율순")

    private val filterAdapter by lazy { FilterSpinnerAdapter(requireContext()) }

    private val gridLayoutManager by lazy { GridLayoutManager(requireContext(), 2) }
    private val linearLayoutManager by lazy { LinearLayoutManager(requireContext()) }

    private val gridItemDecoration by lazy { GridItemDecoration(30.dpToPx(), 16.dpToPx()) }
    private val verticalItemDecoration by lazy { VerticalItemDecoration(30.dpToPx(), 16.dpToPx()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observeData()
        setListener()

        viewModel.getDishes(Source.MAIN)
    }

    private fun initView() {
        binding.viewModel = viewModel
        binding.rvMaindishes.adapter = dishAdapter
        binding.rvMaindishes.layoutManager = gridLayoutManager
        binding.rvMaindishes.addItemDecoration(gridItemDecoration)
        binding.rvMaindishes.itemAnimator = null

        binding.spFilter.adapter = filterAdapter
        filterAdapter.submitList(spinnerItems, 0)
    }

    private fun observeData() {
        viewModel.mainDishes
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
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

        viewModel.isGridMode.observe(viewLifecycleOwner) {
            if (dishAdapter.isGrid != it) {
                if (it) {
                    dishAdapter.isGrid = true
                    binding.rvMaindishes.removeItemDecoration(verticalItemDecoration)
                    binding.rvMaindishes.layoutManager = gridLayoutManager
                    binding.rvMaindishes.addItemDecoration(gridItemDecoration)
                } else {
                    dishAdapter.isGrid = false
                    binding.rvMaindishes.removeItemDecoration(gridItemDecoration)
                    binding.rvMaindishes.layoutManager = linearLayoutManager
                    binding.rvMaindishes.addItemDecoration(verticalItemDecoration)
                }
            }
        }

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