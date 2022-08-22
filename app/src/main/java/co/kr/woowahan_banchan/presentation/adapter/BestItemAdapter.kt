package co.kr.woowahan_banchan.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.ItemBestBinding
import co.kr.woowahan_banchan.databinding.ItemBestHeaderBinding
import co.kr.woowahan_banchan.domain.entity.dish.BestItem
import co.kr.woowahan_banchan.domain.entity.dish.Dish
import co.kr.woowahan_banchan.presentation.decoration.HorizontalItemDecoration
import co.kr.woowahan_banchan.presentation.decoration.HorizontalLayoutManager
import co.kr.woowahan_banchan.presentation.listener.TouchInterceptorListener
import co.kr.woowahan_banchan.util.dpToPx

class BestItemAdapter(
    private val moveToDetail: (String, String) -> Unit,
    private val openBottomSheet: (Dish) -> Unit
) : ListAdapter<BestItem, RecyclerView.ViewHolder>(BestItemDiffCallback()) {

    class BestItemViewHolder(
        private val binding: ItemBestBinding,
        private val parent: ViewGroup,
        private val moveToDetail: (String, String) -> Unit,
        private val openBottomSheet: (Dish) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BestItem) {
            binding.tvName.text = item.name
            val adapter = DishAdapter(moveToDetail, openBottomSheet)
            val layoutManager = HorizontalLayoutManager(context = parent.context, ratio = 0.6f)

            binding.rvItems.adapter = adapter
            binding.rvItems.layoutManager = layoutManager
            if (binding.rvItems.itemDecorationCount == 0)
                binding.rvItems.addItemDecoration(HorizontalItemDecoration(16.dpToPx()))
            binding.rvItems.addOnItemTouchListener(TouchInterceptorListener(parent.context, parent))

            adapter.submitList(item.items.toMutableList())
        }

        companion object {
            fun create(
                parent: ViewGroup,
                moveToDetail: (String, String) -> Unit,
                openBottomSheet: (Dish) -> Unit
            ): BestItemViewHolder {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_best, parent, false)
                val binding = ItemBestBinding.bind(view)
                return BestItemViewHolder(binding, parent, moveToDetail, openBottomSheet)
            }
        }
    }

    class HeaderViewHolder(binding: ItemBestHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun create(parent: ViewGroup): HeaderViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_best_header, parent, false)
                val binding = ItemBestHeaderBinding.bind(view)
                return HeaderViewHolder(binding)
            }
        }
    }

    class BestItemDiffCallback : DiffUtil.ItemCallback<BestItem>() {
        override fun areItemsTheSame(oldItem: BestItem, newItem: BestItem): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: BestItem, newItem: BestItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER -> HeaderViewHolder.create(parent)
            else -> BestItemViewHolder.create(parent, moveToDetail, openBottomSheet)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BestItemViewHolder) {
            holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    companion object {
        const val HEADER = 0
    }
}