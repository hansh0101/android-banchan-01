package co.kr.woowahan_banchan.presentation.ui.cart

import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.FragmentCartBinding
import co.kr.woowahan_banchan.presentation.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_cart

}