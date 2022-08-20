package co.kr.woowahan_banchan.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.ItemCartBinding
import co.kr.woowahan_banchan.databinding.LayoutCartInfoBinding
import co.kr.woowahan_banchan.databinding.LayoutCartRecentlyViewedBinding
import co.kr.woowahan_banchan.domain.entity.cart.CartItem
import co.kr.woowahan_banchan.domain.entity.history.HistoryItem
import co.kr.woowahan_banchan.presentation.decoration.HorizontalItemDecoration
import co.kr.woowahan_banchan.presentation.decoration.HorizontalLayoutManager
import co.kr.woowahan_banchan.presentation.ui.widget.NumberPickerDialog
import co.kr.woowahan_banchan.util.ImageLoader
import co.kr.woowahan_banchan.util.dpToPx
import co.kr.woowahan_banchan.util.toPriceFormat

class CartAdapter(
    private val onHistoryItemClick: (String, String) -> Unit,
    private val onOrderBtnClick: (List<CartItem>) -> Unit,
    private val onFullRecentlyBtnClick: () -> Unit,
    private val onCheckBtnClick: (List<CartItem>) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val cartItems = mutableListOf<CartItem>()

    private val historyAdapter = RecentlyViewedAdapter(
        {
            onHistoryItemClick(it.title, it.detailHash)
        },
        {
            //do nothing
        },
        true
    )

    fun updateCartItems(list: List<CartItem>) {
        val diffUtilCallBack = CartItemDiffUtil(cartItems, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallBack)
        cartItems.run {
            clear()
            addAll(list)
            diffResult.dispatchUpdatesTo(this@CartAdapter)
            notifyItemChanged(size)
        }
    }

    fun getCartItems(): List<CartItem> = cartItems

    fun updateHistoryItems(list: List<HistoryItem>) {
        historyAdapter.submitList(list.toMutableList())
    }

    fun setAllItemsSelected(isSelected: Boolean) {
        cartItems.forEach {
            it.isSelected = isSelected
        }
        notifyDataSetChanged()
    }

    fun deleteSelectedItems() {
        cartItems.removeIf { it.isSelected }
        notifyDataSetChanged()
    }

    class CartItemDiffUtil(
        private val oldItems: List<CartItem>,
        private val newItems: List<CartItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition].hash == newItems[newItemPosition].hash
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition] == newItems[newItemPosition]
        }
    }

    class CartItemViewHolder(
        private val binding: ItemCartBinding,
        private val onCheckBtnClick: (List<CartItem>) -> Unit,
        private val adapter: CartAdapter,
        private val parent: ViewGroup
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItem) {
            binding.cartItem = item
            ImageLoader.loadImage(item.imageUrl) { bitmap ->
                bitmap?.let { binding.ivImage.setImageBitmap(it) }
            }
            binding.ivCheck.setOnClickListener {
                item.isSelected = !item.isSelected
                adapter.notifyItemChanged(adapterPosition)
                adapter.notifyItemChanged(adapter.cartItems.size)
                onCheckBtnClick(adapter.cartItems)
            }
            binding.ivClose.setOnClickListener {
                adapter.cartItems.remove(item)
                adapter.notifyDataSetChanged()
            }
            binding.ivPlus.setOnClickListener {
                item.amount++
                adapter.notifyItemChanged(adapterPosition)
                adapter.notifyItemChanged(adapter.cartItems.size)
            }
            binding.ivMinus.setOnClickListener {
                if (item.amount != 1) {
                    item.amount--
                    adapter.notifyItemChanged(adapterPosition)
                    adapter.notifyItemChanged(adapter.cartItems.size)
                }
            }

            binding.tvAmountValue.setOnClickListener {
                NumberPickerDialog(parent.context, item.amount) { amount ->
                    item.amount = amount
                    adapter.notifyItemChanged(adapterPosition)
                    adapter.notifyItemChanged(adapter.cartItems.size)
                }.show()
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onCheckBtnClick: (List<CartItem>) -> Unit,
                cartAdapter: CartAdapter
            ): CartItemViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_cart, parent, false)
                val binding = ItemCartBinding.bind(view)
                return CartItemViewHolder(binding, onCheckBtnClick, cartAdapter, parent)
            }
        }
    }

    class CartInfoViewHolder(
        private val binding: LayoutCartInfoBinding,
        private val onOrderBtnClick: (List<CartItem>) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItems: List<CartItem>) {
            val menuPrice = cartItems.filter { it.isSelected }.sumOf { it.price * it.amount }
            val deliveryFee = if (menuPrice >= 40000) 0 else 2500
            val totalPrice = menuPrice + deliveryFee
            binding.tvMenuPriceValue.text = "${menuPrice.toPriceFormat()}원"
            binding.tvTotalPriceValue.text = "${totalPrice.toPriceFormat()}원"

            binding.tvMenuDeliveryFeeLabel.isVisible = deliveryFee != 0
            binding.tvMenuDeliveryFeeValue.isVisible = deliveryFee != 0
            binding.tvInsufficientPrice.isVisible = deliveryFee != 0

            if (deliveryFee != 0) {
                binding.tvMenuDeliveryFeeValue.text = "${deliveryFee.toPriceFormat()}원"
                binding.tvInsufficientPrice.text =
                    "${(40000 - menuPrice).toPriceFormat()}원을 더 담으면 무료배송!"
            }

            binding.btnOrder.text = "${totalPrice.toPriceFormat()}원 주문하기"

            binding.btnOrder.isEnabled = totalPrice >= 10000

            binding.btnOrder.setOnClickListener {
                onOrderBtnClick(cartItems)
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onOrderBtnClick: (List<CartItem>) -> Unit
            ): CartInfoViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_cart_info, parent, false)
                val binding = LayoutCartInfoBinding.bind(view)
                return CartInfoViewHolder(binding, onOrderBtnClick)
            }
        }
    }

    class HistoryItemViewHolder(
        private val binding: LayoutCartRecentlyViewedBinding,
        private val parent: ViewGroup,
        private val onFullRecentlyBtnClick: () -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(adapter: RecentlyViewedAdapter) {
            binding.rvRecentlyViewed.adapter = adapter
            binding.rvRecentlyViewed.layoutManager =
                HorizontalLayoutManager(context = parent.context, ratio = 0.4f)
            if (binding.rvRecentlyViewed.itemDecorationCount == 0)
                binding.rvRecentlyViewed.addItemDecoration(HorizontalItemDecoration(8.dpToPx()))
            binding.tvFullView.setOnClickListener {
                onFullRecentlyBtnClick()
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onFullRecentlyBtnClick: () -> Unit
            ): HistoryItemViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_cart_recently_viewed, parent, false)
                val binding = LayoutCartRecentlyViewedBinding.bind(view)
                return HistoryItemViewHolder(binding, parent, onFullRecentlyBtnClick)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            FOOT -> CartInfoViewHolder.create(parent, onOrderBtnClick)
            TAIL -> HistoryItemViewHolder.create(parent, onFullRecentlyBtnClick)
            else -> CartItemViewHolder.create(parent, onCheckBtnClick, this)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CartItemViewHolder -> {
                holder.bind(cartItems[position])
            }
            is CartInfoViewHolder -> {
                holder.bind(cartItems)
            }
            is HistoryItemViewHolder -> {
                holder.bind(historyAdapter)
            }
        }
    }

    override fun getItemCount(): Int = cartItems.size + 2

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            cartItems.size -> FOOT
            cartItems.size + 1 -> TAIL
            else -> BODY
        }
    }

    companion object {
        const val BODY = 0
        const val FOOT = 1
        const val TAIL = 2
    }
}