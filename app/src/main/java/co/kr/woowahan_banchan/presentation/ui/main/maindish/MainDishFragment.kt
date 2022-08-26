package co.kr.woowahan_banchan.presentation.ui.main.maindish

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.FragmentMainDishBinding
import co.kr.woowahan_banchan.presentation.adapter.DishAdapter
import co.kr.woowahan_banchan.presentation.adapter.FilterSpinnerAdapter
import co.kr.woowahan_banchan.presentation.decoration.GridItemDecoration
import co.kr.woowahan_banchan.presentation.decoration.VerticalItemDecoration
import co.kr.woowahan_banchan.presentation.ui.base.BaseFragment
import co.kr.woowahan_banchan.presentation.ui.productdetail.ProductDetailActivity
import co.kr.woowahan_banchan.presentation.ui.widget.CartAddBottomSheet
import co.kr.woowahan_banchan.presentation.ui.widget.ErrorDialog
import co.kr.woowahan_banchan.presentation.viewmodel.UiEvent
import co.kr.woowahan_banchan.presentation.viewmodel.UiState
import co.kr.woowahan_banchan.presentation.viewmodel.main.MainDishViewModel
import co.kr.woowahan_banchan.util.dpToPx
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class MainDishFragment : BaseFragment<FragmentMainDishBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_main_dish

    private val viewModel by viewModels<MainDishViewModel>()

    private val dishAdapter by lazy {
        DishAdapter(
            moveToDetail = { title, hash ->
                startDetailActivity(title, hash)
            },
            openBottomSheet = {
                CartAddBottomSheet.newInstance(it.toSelectedDish())
                    .show(parentFragmentManager, null)
            }
        )
    }

    private val spinnerItems = listOf("기본 정렬순", "금액 높은순", "금액 낮은순", "할인율순")
    private val filterAdapter by lazy { FilterSpinnerAdapter(requireContext()) }
    private val gridLayoutManager by lazy { GridLayoutManager(requireContext(), 2) }
    private val linearLayoutManager by lazy { LinearLayoutManager(requireContext()) }
    private val gridItemDecoration by lazy { GridItemDecoration(0, 30.dpToPx(), 16.dpToPx()) }
    private val verticalItemDecoration by lazy { VerticalItemDecoration(30.dpToPx(), 16.dpToPx()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
        setListener()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getDishes()
    }

    override fun onPause() {
        viewModel.cancelCollectJob()
        super.onPause()
    }

    private fun initView() {
        binding.viewModel = viewModel
        with(binding.rvMaindishes) {
            adapter = dishAdapter
            layoutManager = gridLayoutManager
            addItemDecoration(gridItemDecoration)
            itemAnimator = null
        }
        with(binding.spFilter) {
            adapter = filterAdapter
            setDropDownStartEvent { setBackgroundResource(R.drawable.bg_spinner_openned) }
            setDropDownEndEvent { setBackgroundResource(R.drawable.bg_spinner_default) }
        }
        filterAdapter.submitList(spinnerItems, 0)
    }

    private fun observeData() {
        viewModel.mainDishes
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                binding.pbLoading.isVisible = it is UiState.Init
                when (it) {
                    is UiState.Init -> {}
                    is UiState.Success -> {
                        viewModel.setSortedDishes(binding.spFilter.selectedItemPosition)
                    }
                    is UiState.Error -> {}
                }
            }.launchIn(lifecycleScope)

        viewModel.mainDishesEvent
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                if (it is UiEvent.Error) {
                    Timber.e(it.error)
                    showErrorDialog(it)
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.isGridMode
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                if (dishAdapter.isGrid != it) {
                    dishAdapter.isGrid = it
                    if (it) {
                        binding.rvMaindishes.removeItemDecoration(verticalItemDecoration)
                        binding.rvMaindishes.layoutManager = gridLayoutManager
                        binding.rvMaindishes.addItemDecoration(gridItemDecoration)
                    } else {
                        binding.rvMaindishes.removeItemDecoration(gridItemDecoration)
                        binding.rvMaindishes.layoutManager = linearLayoutManager
                        binding.rvMaindishes.addItemDecoration(verticalItemDecoration)
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.sortedDishes
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                dishAdapter.submitList(it.toMutableList())
                setAdapterDataObserver()
            }.launchIn(viewLifecycleOwner.lifecycleScope)
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

    private fun setAdapterDataObserver() {
        dishAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                scrollToTop()
            }

            private fun scrollToTop() {
                dishAdapter.unregisterAdapterDataObserver(this)
                with(binding.rvMaindishes) {
                    post {
                        scrollToPosition(0)
                    }
                }
            }
        })
    }

    private fun showErrorDialog(event: UiEvent.Error) {
        ErrorDialog(requireContext(), event.error, { viewModel.reFetchDishes() }).show()
    }

    private fun startDetailActivity(title: String, hash: String) {
        startActivity(ProductDetailActivity.getIntent(requireContext(), title, hash))
    }
}