package com.imageslider.android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.squareup.picasso.Picasso
import android.graphics.drawable.Drawable
import android.graphics.Bitmap
import android.widget.ImageView
import com.squareup.picasso.Target
import java.lang.Exception


class ImageSliderAdapter(private val items: List<String>, private var imageSliderListener: ImageSliderListener? = null): PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.layout_silder_image, container, false)
        val image = view.findViewById<ImageView>(R.id.imageItem)
        image.setOnClickListener { imageView ->
            imageSliderListener?.onImageClicked(imageView as ImageView)
        }

        Picasso.get()
                .load(items[position])
                //.error(R.drawable.vector_ic_image_loading)
                //.placeholder(R.drawable.vector_ic_image_loading)
                .into(getTarget(image))
        container.addView(view)

        return view
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return items.count()
    }

    private fun getTarget(imgTarget: ImageView): Target {
        return object : com.squareup.picasso.Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                //
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                //
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                imgTarget.setImageBitmap(bitmap)
                imageSliderListener?.onImageLoaded(bitmap)
            }
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
        super.destroyItem(container, position, `object`)
    }
}