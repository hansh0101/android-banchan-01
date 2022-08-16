package co.kr.woowahan_banchan.presentation.ui.main.best

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.FragmentBestBinding
import co.kr.woowahan_banchan.domain.entity.dish.Dish
import co.kr.woowahan_banchan.domain.entity.dish.SelectedDish
import co.kr.woowahan_banchan.presentation.adapter.BestItemAdapter
import co.kr.woowahan_banchan.presentation.adapter.DishAdapter
import co.kr.woowahan_banchan.presentation.decoration.VerticalItemDecoration
import co.kr.woowahan_banchan.presentation.ui.base.BaseFragment
import co.kr.woowahan_banchan.presentation.ui.productdetail.ProductDetailActivity
import co.kr.woowahan_banchan.presentation.ui.widget.CartAddBottomSheet
import co.kr.woowahan_banchan.presentation.viewmodel.UiState
import co.kr.woowahan_banchan.presentation.viewmodel.main.BestViewModel
import co.kr.woowahan_banchan.util.dpToPx
import co.kr.woowahan_banchan.util.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class BestFragment : BaseFragment<FragmentBestBinding>() {

    override val layoutRes: Int
        get() = R.layout.fragment_best

    private val viewModel by viewModels<BestViewModel>()
    private val bestAdapter = BestItemAdapter(object : DishAdapter.DishClickListener {
        override fun moveToDetail(title: String, hash: String) {
            startDetailActivity(title, hash)
        }

        override fun openBottomSheet(dish: Dish) {
            CartAddBottomSheet.newInstance(SelectedDish(dish)).show(parentFragmentManager, null)
        }
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observeData()

        viewModel.getBests()
    }

    private fun initView() {
        binding.rvBests.adapter = bestAdapter
        binding.rvBests.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBests.addItemDecoration(VerticalItemDecoration(24.dpToPx(),0.dpToPx()))
    }

    private fun observeData() {
        viewModel.bestItems
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is UiState.Init -> {
                        binding.pbLoading.visibility = View.VISIBLE
                    }
                    is UiState.Success -> {
                        bestAdapter.submitList(it.data.toMutableList())
                        binding.pbLoading.visibility = View.GONE
                    }
                    is UiState.Error -> {
                        binding.pbLoading.visibility = View.GONE
                        requireContext().shortToast(it.message)
                    }
                }
            }.launchIn(lifecycleScope)
    }

    private fun startDetailActivity(title: String, hash: String) {
        startActivity(ProductDetailActivity.getIntent(requireContext(), title, hash))
    }
}