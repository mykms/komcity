package ru.komcity.mobile.common

import ru.komcity.mobile.viewModel.ImageLoader.ImageCropType

class Constants {

    companion object {
        val DEFAULT_CROP_TYPE = ImageCropType.CROP_ROUNDED
        const val EXTRA_NEWS_URL = "EXTRA_NEWS_URL"
        const val EXTRA_NEWS_ID = "EXTRA_NEWS_ID"
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_FORUM_NAME = "EXTRA_FORUM_NAME"
        const val EXTRA_SUB_FORUM_ID = "EXTRA_SUB_FORUM_ID"
        const val EXTRA_ANNOUNCEMENTS_ID = "EXTRA_ANNOUNCEMENTS_ID"
        const val BASE_URL = "http://192.168.43.120:8080/komcity/"

        const val SCREEN_NAME_NEWS_LIST = "SCREEN_NAME_NEWS_LIST"
        const val SCREEN_NAME_NEWS_DETAILS = "SCREEN_NAME_NEWS_DETAILS"
        const val SCREEN_NAME_FORUM = "SCREEN_NAME_FORUM"
        const val SCREEN_NAME_FORUM_DETAILS = "SCREEN_NAME_FORUM_DETAILS"
        const val SCREEN_NAME_FORUM_MESSAGES = "SCREEN_NAME_FORUM_MESSAGES"
        const val SCREEN_NAME_ANNOUNCEMENT_FILTER = "SCREEN_NAME_ANNOUNCEMENT_FILTER"
        const val SCREEN_NAME_ANNOUNCEMENTS = "SCREEN_NAME_ANNOUNCEMENTS"

        const val REQUEST_PERMISSION_STORAGE = 100
    }
}