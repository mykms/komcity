package ru.komcity.mobile.Common

import ru.komcity.mobile.Model.ImageLoader.ImageCropType

class Constants {

    companion object {
        val DEFAULT_CROP_TYPE = ImageCropType.CROP_ROUNDED
        val EXTRA_NEWS_ITEM = "EXTRA_NEWS_ITEM"
    }

    class Address {

        companion object {
            open val domen = "komcity.ru"
            open val rootAddress = "http://$domen/"
            open val rootAddressNews = "news/"
            open val rootAddressForum = "forum/"
        }
    }
}