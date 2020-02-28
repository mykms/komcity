package ru.komcity.mobile.common

import ru.komcity.mobile.viewModel.ImageLoader.ImageCropType

class Constants {

    companion object {
        val DEFAULT_CROP_TYPE = ImageCropType.CROP_ROUNDED
        const val EXTRA_NEWS_ITEM = "EXTRA_NEWS_ITEM"
        const val EXTRA_FORUM_ID = "EXTRA_FORUM_ID"
        const val BASE_URL = "http://orderbrains.ru:8080/komcity/"
    }
}