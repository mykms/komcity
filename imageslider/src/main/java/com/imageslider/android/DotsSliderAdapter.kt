package com.imageslider.android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_silder_image.view.*

/**
 * Created by Aleksei Kholoimov on 19.04.2020
 * <p>
 * Adapter for sliding-view
 */
class DotsSliderAdapter(private val items: List<String>, private var onImageClick: (imageUrl: String) -> Unit)
    : RecyclerView.Adapter<DotsSliderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_silder_image, parent)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(imageUrl: String) {
            Picasso.get()
                    .load(imageUrl)
                    .error(R.drawable.imageslider_ic_image_loading)
                    .placeholder(R.drawable.imageslider_ic_image_loading)
                    .into(itemView.imageItem)
            onImageClick(imageUrl)
        }
    }
}