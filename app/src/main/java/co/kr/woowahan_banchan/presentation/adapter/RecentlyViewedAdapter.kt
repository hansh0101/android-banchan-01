package co.kr.woowahan_banchan.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.ItemRecentlyViewedBinding
import co.kr.woowahan_banchan.domain.entity.history.HistoryItem
import co.kr.woowahan_banchan.util.ImageLoader
import co.kr.woowahan_banchan.util.calculateDiff
import co.kr.woowahan_banchan.util.toPriceFormat
import java.util.*

class RecentlyViewedAdapter(
    private val itemClick: (HistoryItem) -> Unit,
    private val cartClick: (HistoryItem) -> Unit,
    private val isPreview : Boolean = false
) :
    ListAdapter<HistoryItem, RecentlyViewedAdapter.RecentlyViewedViewHolder>(diffCallback) {
    class RecentlyViewedViewHolder(
        private val binding: ItemRecentlyViewedBinding,
        private val itemClick: (HistoryItem) -> Unit,
        private val cartClick: (HistoryItem) -> Unit,
        private val isPreview: Boolean
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(historyItem: HistoryItem) {
            with(historyItem) {
                ImageLoader.loadImage(historyItem.imageUrl) {
                    if (it != null) {
                        binding.ivImage.setImageBitmap(it)
                    }
                }

                binding.ibAddBtn.isVisible = !isPreview

                binding.ibAddBtn.setBackgroundResource(
                    if (this.isAdded) {
                        R.drawable.ic_cart_btn_added
                    } else {
                        R.drawable.ic_cart_btn
                    }
                )
                binding.tvTitle.text = this.title
                binding.tvSalePrice.text = this.sPrice.toPriceFormat() + "원"
                if (this.nPrice != null) {
                    binding.tvOriginalPrice.text = this.nPrice.toPriceFormat() + "원"
                    binding.tvOriginalPrice.isVisible = true
                } else {
                    binding.tvOriginalPrice.isVisible = false
                }
                binding.tvTime.text = Date().time.calculateDiff(this.time)
            }
            binding.root.setOnClickListener { itemClick(historyItem) }
            binding.ibAddBtn.setOnClickListener { cartClick(historyItem) }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                itemClick: (HistoryItem) -> Unit,
                cartClick: (HistoryItem) -> Unit,
                isPreview: Boolean
            ): RecentlyViewedViewHolder {
                val binding = ItemRecentlyViewedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return RecentlyViewedViewHolder(binding, itemClick, cartClick, isPreview)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentlyViewedViewHolder =
        RecentlyViewedViewHolder.create(parent, itemClick, cartClick, isPreview)

    override fun onBindViewHolder(holder: RecentlyViewedViewHolder, position: Int) =
        holder.onBind(currentList[position])

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<HistoryItem>() {
            override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean =
                oldItem.detailHash == newItem.detailHash

            override fun areContentsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean =
                oldItem == newItem
        }
    }
}