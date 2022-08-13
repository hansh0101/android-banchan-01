package co.kr.woowahan_banchan.presentation.ui.main.otherdish

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.FragmentOtherDishBinding
import co.kr.woowahan_banchan.domain.repository.Source
import co.kr.woowahan_banchan.presentation.adapter.DishAdapter
import co.kr.woowahan_banchan.presentation.adapter.FilterSpinnerAdapter
import co.kr.woowahan_banchan.presentation.decoration.GridItemDecoration
import co.kr.woowahan_banchan.presentation.ui.base.BaseFragment
import co.kr.woowahan_banchan.presentation.ui.productdetail.ProductDetailActivity
import co.kr.woowahan_banchan.presentation.viewmodel.UiState
import co.kr.woowahan_banchan.presentation.viewmodel.main.OtherDishViewModel
import co.kr.woowahan_banchan.util.dpToPx
import co.kr.woowahan_banchan.util.shortToast
import co.kr.woowahan_banchan.util.stringArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class OtherDishFragment : BaseFragment<FragmentOtherDishBinding>() {

    override val layoutRes: Int
        get() = R.layout.fragment_other_dish

    private val dishType by stringArgs()

    private val viewModel by viewModels<OtherDishViewModel>()

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
        initData()

        binding.viewModel = viewModel
    }

    private fun initData() {
        when (dishType) {
            SOUP -> {
                viewModel.getDishes(Source.SOUP)
                viewModel.setTitle("정성이 담긴\n뜨끈뜨끈 국물 요리")
            }
            SIDE -> {
                viewModel.getDishes(Source.SIDE)
                viewModel.setTitle("식탁을 풍성하게 하는\n정갈한 밑반찬")
            }
        }
    }

    private fun initView() {
        binding.rvDishes.adapter = dishAdapter
        binding.rvDishes.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvDishes.addItemDecoration(
            GridItemDecoration(30.dpToPx(), 16.dpToPx())
        )
        binding.rvDishes.itemAnimator = null

        binding.spFilter.adapter = filterAdapter
        filterAdapter.submitList(spinnerItems, 0)
    }

    private fun observeData() {
        viewModel.otherDishes.flowWithLifecycle(viewLifecycleOwner.lifecycle)
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

    companion object {
        const val SOUP = "soup"
        const val SIDE = "side"

        fun newInstance(dishType: String): OtherDishFragment {
            return OtherDishFragment().apply {
                arguments = Bundle().apply {
                    putString("dishType", dishType)
                }
            }
        }
    }
}