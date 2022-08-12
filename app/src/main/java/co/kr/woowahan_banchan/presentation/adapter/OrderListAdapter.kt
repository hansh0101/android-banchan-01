package co.kr.woowahan_banchan.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.ItemOrderListBinding
import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderHistory

class OrderListAdapter :
    ListAdapter<OrderHistory, OrderListAdapter.OrderListViewHolder>(diffCallback) {
    class OrderListViewHolder(private val binding: ItemOrderListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(orderHistory: OrderHistory) {

        }

        companion object {
            fun create(parent: ViewGroup): OrderListViewHolder {
                val binding = DataBindingUtil.inflate<ItemOrderListBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_order_list,
                    parent,
                    false
                )
                return OrderListViewHolder(binding)
            }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<OrderHistory>() {
            override fun areItemsTheSame(oldItem: OrderHistory, newItem: OrderHistory): Boolean =
                oldItem.orderId == newItem.orderId

            override fun areContentsTheSame(oldItem: OrderHistory, newItem: OrderHistory): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListViewHolder =
        OrderListViewHolder.create(parent)

    override fun onBindViewHolder(holder: OrderListViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }
}