package ru.komcity.mobile.common.analytic

/**
 * Created by Aleksei Kholoimov on 16.05.2020
 * <p>
 * Ключи (имена) для метрик
 */
class AnalyticTags {

    companion object {
        const val CLIENT_ID = "client_id"

        const val SCREEN_OPEN = "SCREEN_OPEN"
        const val NEWS_DETAIL = "NEWS_DETAIL"
        const val FORUM_DETAIL = "FORUM_DETAIL"
        const val FORUM_MESSAGES = "FORUM_MESSAGES"
        const val ANNOUNCEMENT_SHOW = "ANNOUNCEMENT_SHOW"
        const val ANNOUNCEMENT_DETAIL = "ANNOUNCEMENT_DETAIL"
        const val SHARE_CLICK = "SHARE_CLICK"
        const val SHARE_COMPLETE = "SHARE_COMPLETE"
        const val COPY_TEXT_CLICK = "COPY_TEXT_CLICK"
    }
}

class AnalyticParamTags {

    companion object {
        const val SCREEN_NAME = "SCREEN_NAME"
        const val NEWS_ID = "NEWS_ID"
        const val FORUM_NAME = "FORUM_NAME"
        const val FORUM_ID = "FORUM_ID"
        const val ANNOUNCEMENT_LIST_ID = "ANNOUNCEMENT_LIST_ID"
        const val ANNOUNCEMENT_ID = "ANNOUNCEMENT_ID"
        const val SOCIAL_APP_NAME = "SOCIAL_APP_NAME"
    }
}