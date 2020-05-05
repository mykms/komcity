package com.imageslider.android

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Aleksei Kholoimov on 19.04.2020
 * <p>
 * Adapter for sliding-view
 */
internal class DotsSliderAdapter(private val items: ArrayList<Boolean>,
                        @DrawableRes
                        private val activeBackgroundRes: Int,
                        @DrawableRes
                        private val inActiveBackgroundRes: Int,
                        private val activeItemSize: Int,
                        private val inActiveItemSize: Int,
                        private var onImageClick: (position: Int) -> Unit)
    : RecyclerView.Adapter<DotsSliderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = View(parent.context).apply {
            layoutParams = LinearLayout.LayoutParams(activeItemSize.toDp, activeItemSize.toDp).apply {
                setMargins(0.toDp, 0.toDp, 0.toDp, 0.toDp)
            }
            setBackgroundResource(activeBackgroundRes)
        }
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    fun changeItemAndUpdate(position: Int, item: Boolean) {
        items[position] = item
        notifyItemChanged(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(isSelected: Boolean, position: Int) {
            if (isSelected) {
                itemView.apply {
                    setBackgroundResource(activeBackgroundRes)
                    layoutParams = LinearLayout.LayoutParams(activeItemSize.toDp, activeItemSize.toDp).apply {
                        setMargins(8.toDp, 0.toDp, 8.toDp, 0.toDp)
                    }
                }
            } else {
                itemView.apply {
                    setBackgroundResource(inActiveBackgroundRes)
                    layoutParams = LinearLayout.LayoutParams(inActiveItemSize.toDp, inActiveItemSize.toDp).apply {
                        setMargins(8.toDp, 0.toDp, 8.toDp, 0.toDp)
                    }
                }
            }
            itemView.setOnClickListener {
                onImageClick(position)
            }
        }
    }
}