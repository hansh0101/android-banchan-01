package co.kr.woowahan_banchan.presentation.ui.main.best

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.FragmentBestBinding
import co.kr.woowahan_banchan.presentation.adapter.BestItemAdapter
import co.kr.woowahan_banchan.presentation.ui.base.BaseFragment
import co.kr.woowahan_banchan.presentation.ui.productdetail.ProductDetailActivity
import co.kr.woowahan_banchan.presentation.viewmodel.MainViewModel
import co.kr.woowahan_banchan.presentation.viewmodel.MainViewModel.UiState
import kotlinx.coroutines.launch

class BestFragment : BaseFragment<FragmentBestBinding>() {

    override val layoutRes: Int
        get() = R.layout.fragment_best

    private val activityViewModel by activityViewModels<MainViewModel>()
    private val bestAdapter = BestItemAdapter { title, hash -> startDetailActivity(title, hash) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.rvBests.adapter = bestAdapter
        binding.rvBests.layoutManager = LinearLayoutManager(requireContext())

        activityViewModel.getBests()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                activityViewModel.uiState.collect() { state ->
                    when (state) {
                        is UiState.Success -> {
                            bestAdapter.submitList(state.list)
                            binding.pbLoading.visibility = View.GONE
                        }
                        is UiState.Error -> {
                            binding.pbLoading.visibility = View.GONE
                        }
                        else -> {
                            binding.pbLoading.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun startDetailActivity(title: String, hash: String) {
        startActivity(ProductDetailActivity.getIntent(requireContext(), title, hash))
    }
}