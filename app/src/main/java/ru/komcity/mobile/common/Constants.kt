package ru.komcity.mobile.common

import ru.komcity.mobile.viewModel.ImageLoader.ImageCropType

class Constants {

    companion object {
        val DEFAULT_CROP_TYPE = ImageCropType.CROP_ROUNDED
        const val EXTRA_NEWS_ID = "EXTRA_NEWS_ID"
        const val EXTRA_NEWS_TITLE = "EXTRA_NEWS_TITLE"
        const val EXTRA_FORUM_ID = "EXTRA_FORUM_ID"
        const val EXTRA_SUB_FORUM_ID = "EXTRA_SUB_FORUM_ID"
        const val EXTRA_ANNOUNCEMENTS_ID = "EXTRA_ANNOUNCEMENTS_ID"
        const val BASE_URL = "http://192.168.43.199:8080/komcity/"
    }
}