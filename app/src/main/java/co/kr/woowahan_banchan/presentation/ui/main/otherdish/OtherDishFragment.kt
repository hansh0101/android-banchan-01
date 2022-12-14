package co.kr.woowahan_banchan.presentation.ui.main.otherdish

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.FragmentOtherDishBinding
import co.kr.woowahan_banchan.presentation.adapter.DishAdapter
import co.kr.woowahan_banchan.presentation.adapter.FilterSpinnerAdapter
import co.kr.woowahan_banchan.presentation.decoration.GridItemDecoration
import co.kr.woowahan_banchan.presentation.ui.base.BaseFragment
import co.kr.woowahan_banchan.presentation.ui.productdetail.ProductDetailActivity
import co.kr.woowahan_banchan.presentation.ui.widget.CartAddBottomSheet
import co.kr.woowahan_banchan.presentation.ui.widget.ErrorDialog
import co.kr.woowahan_banchan.presentation.viewmodel.UiEvent
import co.kr.woowahan_banchan.presentation.viewmodel.UiState
import co.kr.woowahan_banchan.presentation.viewmodel.main.DishType
import co.kr.woowahan_banchan.presentation.viewmodel.main.OtherDishViewModel
import co.kr.woowahan_banchan.util.dpToPx
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

    private val spinnerItems = listOf("?????? ?????????", "?????? ?????????", "?????? ?????????", "????????????")

    private val filterAdapter by lazy { FilterSpinnerAdapter(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        initView()
        observeData()
        setListener()
        initData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getDishes(dishType)
    }

    override fun onPause() {
        viewModel.cancelCollectJob()
        super.onPause()
    }

    private fun initData() {
        when (dishType) {
            DishType.SOUP.name -> {
                binding.tvTitle.text = "????????? ??????\n???????????? ?????? ??????"
            }
            DishType.SIDE.name -> {
                binding.tvTitle.text = "????????? ???????????? ??????\n????????? ?????????"
            }
        }
        viewModel.getDishes(dishType)
    }

    private fun initView() {
        with(binding.rvDishes) {
            adapter = dishAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(GridItemDecoration(0, 30.dpToPx(), 16.dpToPx()))
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
        viewModel.otherDishes
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

        viewModel.otherDishesEvent
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                if (it is UiEvent.Error) {
                    showErrorDialog(it)
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.sortedDishes
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                dishAdapter.submitList(it.toMutableList())
                setAdapterDataObserver()
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setAdapterDataObserver() {
        dishAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                scrollToTop()
            }

            private fun scrollToTop() {
                dishAdapter.unregisterAdapterDataObserver(this)
                with(binding.rvDishes) {
                    post {
                        scrollToPosition(0)
                    }
                }
            }
        })
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

    private fun showErrorDialog(event: UiEvent.Error) {
        ErrorDialog(requireContext(), event.error, { viewModel.reFetchDishes(dishType) }).show()
    }

    private fun startDetailActivity(title: String, hash: String) {
        startActivity(ProductDetailActivity.getIntent(requireContext(), title, hash))
    }

    companion object {
        fun newInstance(dishType: String): OtherDishFragment {
            return OtherDishFragment().apply {
                arguments = Bundle().apply {
                    putString("dishType", dishType)
                }
            }
        }
    }
}