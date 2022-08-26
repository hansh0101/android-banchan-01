package co.kr.woowahan_banchan.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.ItemOrderListBinding
import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderHistory
import co.kr.woowahan_banchan.util.ImageLoader
import co.kr.woowahan_banchan.util.toPriceFormat

class OrderListAdapter(private val itemClick: (OrderHistory) -> Unit) :
    ListAdapter<OrderHistory, OrderListAdapter.OrderListViewHolder>(diffCallback) {
    class OrderListViewHolder(
        private val binding: ItemOrderListBinding,
        private val itemClick: (OrderHistory) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(orderHistory: OrderHistory) {
            with(orderHistory) {
                ImageLoader(binding.ivThumbnail, itemView.context)
                    .setPlaceHolder(R.mipmap.ic_launcher)
                    .setErrorImage(R.mipmap.ic_launcher)
                    .loadImage(this.thumbnailUrl)

                binding.tvTitle.text = if (this.count > 1) {
                    this.title + " 외 ${this.count - 1}개"
                } else {
                    this.title
                }

                binding.tvPrice.text = this.totalPrice.toPriceFormat() + "원"
                setDeliveryInfo(orderHistory.isCompleted)
            }
            binding.root.setOnClickListener { itemClick(orderHistory) }
        }

        private fun setDeliveryInfo(isCompleted: Boolean) {
            if (isCompleted) {
                with(binding.tvDeliveryInfo) {
                    this.text = "배송완료"
                    this.setTextColor(resources.getColor(R.color.grayscale_000000, null))
                }
            } else {
                with(binding.tvDeliveryInfo) {
                    this.text = "배송 준비중"
                    this.setTextColor(resources.getColor(R.color.primary_f46700, null))
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup, itemClick: (OrderHistory) -> Unit): OrderListViewHolder {
                val binding = ItemOrderListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return OrderListViewHolder(binding, itemClick)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListViewHolder =
        OrderListViewHolder.create(parent, itemClick)

    override fun onBindViewHolder(holder: OrderListViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<OrderHistory>() {
            override fun areItemsTheSame(oldItem: OrderHistory, newItem: OrderHistory): Boolean =
                oldItem.orderId == newItem.orderId

            override fun areContentsTheSame(oldItem: OrderHistory, newItem: OrderHistory): Boolean =
                oldItem == newItem
        }
    }
}