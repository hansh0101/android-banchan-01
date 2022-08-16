package co.kr.woowahan_banchan.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.ItemCartBinding
import co.kr.woowahan_banchan.databinding.LayoutCartInfoBinding
import co.kr.woowahan_banchan.databinding.LayoutCartRecentlyViewedBinding
import co.kr.woowahan_banchan.domain.entity.cart.CartItem
import co.kr.woowahan_banchan.domain.entity.history.HistoryItem
import co.kr.woowahan_banchan.presentation.decoration.HorizontalItemDecoration
import co.kr.woowahan_banchan.presentation.decoration.HorizontalLayoutManager
import co.kr.woowahan_banchan.util.ImageLoader
import co.kr.woowahan_banchan.util.dpToPx
import co.kr.woowahan_banchan.util.toPriceFormat

class CartAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val cartItems = mutableListOf<CartItem>()
    private val historyItems = mutableListOf<HistoryItem>()

    private val historyAdapter = RecentlyViewedAdapter(
        {
            //item click action
        },
        {
            //btn click action
        }
    )

    fun updateCartItems(list: List<CartItem>) {
        cartItems.clear()
        cartItems.addAll(list)
        notifyDataSetChanged()
    }

    fun updateHistoryItems(list: List<HistoryItem>) {
        historyAdapter.submitList(list.toMutableList())
    }

    class CartItemViewHolder(
        private val binding: ItemCartBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItem) {
            binding.cartItem = item
            ImageLoader.loadImage(item.imageUrl) { bitmap ->
                bitmap?.let { binding.ivImage.setImageBitmap(it) }
            }
            binding.ivCheck.setOnClickListener {
                //check click action
            }
            binding.ivClose.setOnClickListener {
                //close click action
            }
            binding.ivPlus.setOnClickListener {
                //plus click action
            }
            binding.ivMinus.setOnClickListener {
                //minus click action
            }
        }

        companion object {
            fun create(parent: ViewGroup): CartItemViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_cart, parent, false)
                val binding = ItemCartBinding.bind(view)
                return CartItemViewHolder(binding)
            }
        }
    }

    class CartInfoViewHolder(
        private val binding: LayoutCartInfoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItems: List<CartItem>) {
            val menuPrice = cartItems.sumOf { it.price }
            val deliveryFee = if (menuPrice >= 40000) 0 else 2500
            val totalPrice = menuPrice + deliveryFee
            binding.tvMenuPriceValue.text = "${menuPrice.toPriceFormat()}원"
            binding.tvTotalPriceValue.text = "${totalPrice.toPriceFormat()}원"

            if (deliveryFee == 0) {
                binding.tvMenuDeliveryFeeLabel.visibility = View.GONE
                binding.tvMenuDeliveryFeeValue.visibility = View.GONE
            } else {
                binding.tvMenuDeliveryFeeLabel.visibility = View.VISIBLE
                binding.tvMenuDeliveryFeeValue.visibility = View.VISIBLE
                binding.tvMenuDeliveryFeeValue.text = "${deliveryFee.toPriceFormat()}원"
                binding.tvInsufficientPrice.text =
                    "${(40000 - menuPrice).toPriceFormat()}원을 더 담으면 무료배송!"
            }

            binding.btnOrder.text = "${totalPrice.toPriceFormat()}원 주문하기"
            binding.btnOrder.setOnClickListener {
                //click action
            }
        }

        companion object {
            fun create(parent: ViewGroup): CartInfoViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_cart_info, parent, false)
                val binding = LayoutCartInfoBinding.bind(view)
                return CartInfoViewHolder(binding)
            }
        }
    }

    class HistoryItemViewHolder(
        private val binding: LayoutCartRecentlyViewedBinding,
        private val parent: ViewGroup
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(adapter: RecentlyViewedAdapter) {
            binding.rvRecentlyViewed.adapter = adapter
            binding.rvRecentlyViewed.layoutManager = HorizontalLayoutManager(context = parent.context, ratio = 0.4f)
            binding.rvRecentlyViewed.addItemDecoration(HorizontalItemDecoration(8.dpToPx()))
            binding.tvFullView.setOnClickListener {

            }
        }

        companion object {
            fun create(parent: ViewGroup): HistoryItemViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_cart_recently_viewed, parent, false)
                val binding = LayoutCartRecentlyViewedBinding.bind(view)
                return HistoryItemViewHolder(binding, parent)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            FOOT -> CartInfoViewHolder.create(parent)
            TAIL -> HistoryItemViewHolder.create(parent)
            else -> CartItemViewHolder.create(parent)
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