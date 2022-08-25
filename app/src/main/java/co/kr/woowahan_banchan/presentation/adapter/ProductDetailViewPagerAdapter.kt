package co.kr.woowahan_banchan.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.kr.woowahan_banchan.R
import co.kr.woowahan_banchan.databinding.ItemProductDetailBinding
import co.kr.woowahan_banchan.util.ImageLoader

class ProductDetailViewPagerAdapter :
    RecyclerView.Adapter<ProductDetailViewPagerAdapter.ImageViewHolder>() {
    private var items: List<String> = listOf()

    class ImageViewHolder(private val binding: ItemProductDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(imageUrl: String) {
            ImageLoader(binding.ivProductDetail,itemView.context)
                .setPlaceHolder(R.mipmap.ic_launcher)
                .setErrorImage(R.mipmap.ic_launcher)
                .loadImage(imageUrl)
        }

        companion object {
            fun create(parent: ViewGroup): ImageViewHolder {
                val binding = ItemProductDetailBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ImageViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder.create(parent)

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) =
        holder.onBind(items[position])

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<String>) {
        items = newItems
        notifyDataSetChanged()
    }
}