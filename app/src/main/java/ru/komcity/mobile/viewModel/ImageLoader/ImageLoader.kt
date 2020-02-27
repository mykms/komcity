package ru.komcity.mobile.viewModel.ImageLoader

import android.net.Uri
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import ru.komcity.mobile.common.Constants
import ru.komcity.mobile.R
import java.io.File

class ImageLoader() {

    private val defaultImageResId = R.drawable.vector_ic_image_loading

    constructor(url: String, imageView: ImageView, cropType: ImageCropType = Constants.DEFAULT_CROP_TYPE) : this() {
        if (url.isNotEmpty()) {
            loadImage(url, imageView, defaultImageResId, defaultImageResId, cropType)
        }
    }

    constructor(uri: Uri?, imageView: ImageView, cropType: ImageCropType = Constants.DEFAULT_CROP_TYPE): this() {
        uri?.let {
            loadImage(it, imageView, defaultImageResId, defaultImageResId, cropType)
        }
    }

    constructor(file: File?, imageView: ImageView, cropType: ImageCropType = Constants.DEFAULT_CROP_TYPE): this() {
        file?.let {
            loadImage(it, imageView, defaultImageResId, defaultImageResId, cropType)
        }
    }

    constructor(iconRes: Int, imageView: ImageView, cropType: ImageCropType = Constants.DEFAULT_CROP_TYPE): this() {
        loadImage(iconRes, imageView, defaultImageResId, defaultImageResId, cropType)
    }

    private fun loadImage(file: File,
                          imageView: ImageView,
                          placeHolderRes: Int,
                          errorRes: Int,
                          cropType: ImageCropType = Constants.DEFAULT_CROP_TYPE) {
        loadImageCreator(Picasso.get().load(file), imageView, placeHolderRes, errorRes, cropType)
    }

    private fun loadImage(uri: Uri,
                          imageView: ImageView,
                          placeHolderRes: Int,
                          errorRes: Int,
                          cropType: ImageCropType = Constants.DEFAULT_CROP_TYPE) {
        loadImageCreator(Picasso.get().load(uri), imageView, placeHolderRes, errorRes, cropType)
    }

    private fun loadImage(url: String,
                          imageView: ImageView,
                          placeHolderRes: Int,
                          errorRes: Int,
                          cropType: ImageCropType = Constants.DEFAULT_CROP_TYPE) {
        if (url.isNotEmpty()) {
            loadImageCreator(Picasso.get().load(url), imageView, placeHolderRes, errorRes, cropType)
        }
    }

    private fun loadImage(iconRes: Int,
                          imageView: ImageView,
                          placeHolderRes: Int,
                          errorRes: Int,
                          cropType: ImageCropType = Constants.DEFAULT_CROP_TYPE) {
        loadImageCreator(Picasso.get().load(iconRes), imageView, placeHolderRes, errorRes, cropType)
    }

    private fun loadImageCreator(creator: RequestCreator,
                                 imageView: ImageView,
                                 placeHolderRes: Int,
                                 errorRes: Int,
                                 cropType: ImageCropType = Constants.DEFAULT_CROP_TYPE) {
        with(creator) {
            placeholder(placeHolderRes)
            error(errorRes)
            //networkPolicy(NetworkPolicy.OFFLINE)
            //memoryPolicy(MemoryPolicy.NO_STORE)
            when(cropType) {
                ImageCropType.CROP_CIRCLE -> {
                    transform(ImageTransform.CIRCLE)
                }
                ImageCropType.CROP_ROUNDED -> {
                    transform(ImageTransform.ROUNDED)
                }
                ImageCropType.CROP_SQUARE -> {
                    //no transformation
                }
                ImageCropType.CROP_NO -> {
                    //no transformation
                }
            }
            into(imageView)
        }
    }
}