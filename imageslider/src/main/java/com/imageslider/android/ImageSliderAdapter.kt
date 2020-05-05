package com.imageslider.android

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.layout_silder_image.view.*

/**
 * Created by Aleksei Kholoimov on 19.04.2020
 * <p>
 * Adapter for sliding-view
 */
internal class ImageSliderAdapter(private val items: List<String>,
                                  private var itemCallback: ImageSliderCallback?)
    : RecyclerView.Adapter<ImageSliderAdapter.ViewHolder>() {

    private val itemsBitmap = arrayOfNulls<Bitmap?>(items.size)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_silder_image, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position, object : HolderImageLoaderResult{
            override fun onImageLoaded(position: Int, bitmap: Bitmap?) {
                itemsBitmap[position] = bitmap
            }
        })
    }

    fun getBitmapByPosition(position: Int): Bitmap? {
        return if (position >= 0 && position < itemsBitmap.size) {
            itemsBitmap[position]
        } else null
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var target: Target

        fun bind(imageUrl: String, position: Int, listener: HolderImageLoaderResult) {
            target = object: Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                    itemView.imageItem.setImageDrawable(placeHolderDrawable)
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    itemView.imageItem.setImageDrawable(errorDrawable)
                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    itemView.imageItem.setImageBitmap(bitmap)
                    itemView.imageItem.setOnClickListener { itemCallback?.onImageClick(bitmap, position) }
                    itemCallback?.onImageLoaded(bitmap, position)
                    listener.onImageLoaded(position, bitmap)
                }
            }
            Picasso.get()
                    .load(imageUrl)
                    .error(R.drawable.imageslider_ic_image_loading)
                    .placeholder(R.drawable.imageslider_ic_image_loading)
                    .into(target)
        }
    }

    interface HolderImageLoaderResult {
        fun onImageLoaded(position: Int, bitmap: Bitmap?)
    }
}