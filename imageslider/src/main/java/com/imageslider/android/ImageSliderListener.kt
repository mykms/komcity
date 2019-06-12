package com.imageslider.android

import android.graphics.Bitmap
import android.widget.ImageView

interface ImageSliderListener {
    fun onImageClicked(image: ImageView)
    fun onImageLoaded(bitmap: Bitmap?)
}