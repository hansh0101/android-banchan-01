package co.kr.woowahan_banchan.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.ItemDishGridBinding
import co.kr.woowahan_banchan.databinding.ItemDishLinearBinding
import co.kr.woowahan_banchan.domain.entity.dish.Dish
import co.kr.woowahan_banchan.util.ImageLoader

class DishAdapter(
    private val clickListener: DishClickListener
) : ListAdapter<Dish, RecyclerView.ViewHolder>(DishDiffCallback()) {

    var isGrid: Boolean = true
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface DishClickListener {
        fun moveToDetail(title: String, hash: String)
        fun openBottomSheet(dish : Dish)
    }

    class DishGridViewHolder(
        private val binding: ItemDishGridBinding,
        private val clickListener: DishClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Dish) {
            ImageLoader.loadImage(item.imageUrl) {
                binding.ivImage.setImageBitmap(it)
                binding.dish = item

                binding.root.setOnClickListener {
                    clickListener.moveToDetail(item.title, item.detailHash)
                }
                binding.ibAddBtn.setOnClickListener {
                    clickListener.openBottomSheet(item)
                }
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                clickListener: DishClickListener
            ): DishGridViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_dish_grid, parent, false)
                val binding = ItemDishGridBinding.bind(view)
                return DishGridViewHolder(binding, clickListener)
            }
        }
    }

    class DishLinearViewHolder(
        private val binding: ItemDishLinearBinding,
        private val clickListener: DishClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Dish) {
            ImageLoader.loadImage(item.imageUrl) {
                binding.ivImage.setImageBitmap(it)
                binding.dish = item

                binding.root.setOnClickListener {
                    clickListener.moveToDetail(item.title, item.detailHash)
                }
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                listener: DishClickListener
            ): DishLinearViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_dish_linear, parent, false)
                val binding = ItemDishLinearBinding.bind(view)
                return DishLinearViewHolder(binding, listener)
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

    override fun getItemViewType(position: Int): Int {
        return if (isGrid) GRID else LINEAR
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == GRID) DishGridViewHolder.create(parent, clickListener)
        else DishLinearViewHolder.create(parent, clickListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DishGridViewHolder -> {
                holder.bind(getItem(position))
            }
            is DishLinearViewHolder -> {
                holder.bind(getItem(position))
            }
        }
    }

    companion object {
        const val GRID = 0
        const val LINEAR = 1
    }
}