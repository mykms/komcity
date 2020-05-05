package com.imageslider.android

import android.graphics.Bitmap

/**
 * Created by Aleksei Kholoimov on 05.05.2020
 * <p>
 * События для отслеживания действия в объекте ImageSlider
 */
interface ImageSliderCallback {
    fun onImageClick(bitmap: Bitmap?, position: Int)
    fun onImageLoaded(bitmap: Bitmap?, position: Int)
    fun onImageSwipe(bitmap: Bitmap?, position: Int)
}