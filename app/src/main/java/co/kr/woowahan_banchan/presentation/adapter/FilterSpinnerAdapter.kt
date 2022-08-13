package co.kr.woowahan_banchan.presentation.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import co.kr.woowahan_banchan.databinding.ItemFilterBinding

class FilterSpinnerAdapter(context: Context) : BaseAdapter() {

    private val items = mutableListOf<FilterItem>()
    private val inflater = LayoutInflater.from(context)

    fun submitList(list: List<String>, position: Int) {
        items.clear()
        for (idx in list.indices) {
            items.add(FilterItem(list[idx], idx == position))
        }
        notifyDataSetChanged()
    }

    data class FilterItem(
        val text: String,
        var isSelected: Boolean = false
    )

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): FilterItem = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val binding = ItemFilterBinding.inflate(inflater, parent, false)
        binding.tvFilter.text = items[position].text
        binding.ivCheck.visibility = View.GONE
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = ItemFilterBinding.inflate(inflater, parent, false)
        binding.tvFilter.text = items[position].text
        if (items[position].isSelected) {
            binding.tvFilter.setTypeface(null, Typeface.BOLD)
            binding.ivCheck.visibility = View.VISIBLE
        } else {
            binding.tvFilter.setTypeface(null, Typeface.NORMAL)
            binding.ivCheck.visibility = View.INVISIBLE
        }
        return binding.root
    }
}