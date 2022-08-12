package co.kr.woowahan_banchan.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.ItemBestBinding
import co.kr.woowahan_banchan.domain.entity.dish.BestItem

class BestItemAdapter : ListAdapter<BestItem, BestItemAdapter.BestItemViewHolder>(BestItemDiffCallback()) {

    class BestItemViewHolder(
        private val binding: ItemBestBinding,
        private val parent: ViewGroup
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BestItem) {
            binding.tvName.text = item.name
            val adapter = DishAdapter(false)
            val layoutManager = LinearLayoutManager(parent.context)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL

            binding.rvItems.adapter = adapter
            binding.rvItems.layoutManager = layoutManager
            adapter.submitList(item.items.toMutableList())
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_best, parent, false)
        val binding = ItemBestBinding.bind(view)
        return BestItemViewHolder(binding, parent)
    }

    override fun onBindViewHolder(holder: BestItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}