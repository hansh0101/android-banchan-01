package co.kr.woowahan_banchan.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.ItemDishBinding
import co.kr.woowahan_banchan.domain.entity.dish.Dish
import co.kr.woowahan_banchan.util.ImageLoader

class DishAdapter(
    private val isGrid: Boolean,
    private val moveToDetail: (String, String) -> Unit
) : ListAdapter<Dish, RecyclerView.ViewHolder>(DishDiffCallback()) {

    class DishLinearViewHolder(
        private val binding: ItemDishBinding,
        private val moveToDetail: (String, String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Dish) {
            ImageLoader.loadImage(item.imageUrl) {
                binding.ivImage.setImageBitmap(it)
                binding.dish = item

                binding.root.setOnClickListener {
                    moveToDetail(item.title, item.detailHash)
                }
            }
        }
    }

    class DishDiffCallback : DiffUtil.ItemCallback<Dish>() {
        override fun areItemsTheSame(oldItem: Dish, newItem: Dish): Boolean {
            return oldItem.detailHash == newItem.detailHash
        }

        override fun areContentsTheSame(oldItem: Dish, newItem: Dish): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dish, parent, false)
        val binding = ItemDishBinding.bind(view)
        return DishLinearViewHolder(binding, moveToDetail)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DishLinearViewHolder).bind(getItem(position))
    }
}