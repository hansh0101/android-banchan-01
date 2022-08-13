package co.kr.woowahan_banchan.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import co.kr.woowahan_banchan.presentation.ui.main.best.BestFragment
import co.kr.woowahan_banchan.presentation.ui.main.maindish.MainDishFragment
import co.kr.woowahan_banchan.presentation.ui.main.otherdish.OtherDishFragment

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return TAB_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            1 -> MainDishFragment()
            2 -> OtherDishFragment.newInstance(OtherDishFragment.SOUP)
            3 -> OtherDishFragment.newInstance(OtherDishFragment.SIDE)
            else -> BestFragment()
        }
    }

    companion object{
        const val TAB_COUNT = 4
    }
}