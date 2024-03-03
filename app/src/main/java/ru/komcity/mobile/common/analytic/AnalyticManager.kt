package ru.komcity.mobile.common.analytic

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Created by Aleksei Kholoimov on 16.05.2020
 * <p>
 * Логирование событий библиотекой Firebase
 */
interface AnalyticManager {

    fun sendEvent(tag: String)
    fun sendEvent(tag: String, params: Bundle = Bundle())
    fun onScreenOpen(screenName: String)
    fun onNewsDetailClick(newsUrl: String)
    fun onForumDetailClick(forumName: String)
    fun onForumMessagesClick(forumId: String)
    fun onAnnouncementShowClick(listId: String)
    fun onAnnouncementDetailsClick(id: Int)
    fun onShareInfoClick(fromScreenName: String)
    fun onShareInfoComplete(fromScreenName: String, appName: String)
    fun onCopyTextClick(fromScreenName: String)
}

class AnalyticManagerImpl(private val clientId: String,
                          private val context: Context): AnalyticManager {

    private var analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)
    private val isGooglePlayServicesAvailable: Boolean
        get() {
            val resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)
            return resultCode == ConnectionResult.SUCCESS
        }

    override fun sendEvent(tag: String) {
        sendKeyValueEvent(tag, Bundle())
    }

    override fun sendEvent(tag: String, params: Bundle) {
        sendKeyValueEvent(tag, params)
    }

    private fun sendKeyValueEvent(keyName: String, args: Bundle = Bundle()) {
        if (isGooglePlayServicesAvailable) {
            with(analytics) {
                logEvent(keyName, args)
                setUserProperty(AnalyticTags.CLIENT_ID, clientId)
            }
        }
    }

    override fun onScreenOpen(screenName: String) {
        sendKeyValueEvent(AnalyticTags.SCREEN_OPEN, bundleOf(AnalyticParamTags.SCREEN_NAME to screenName))
    }

    override fun onNewsDetailClick(newsUrl: String) {
        sendKeyValueEvent(AnalyticTags.NEWS_DETAIL, bundleOf(AnalyticParamTags.NEWS_ID to newsUrl))
    }

    override fun onForumDetailClick(forumName: String) {
        sendKeyValueEvent(AnalyticTags.FORUM_DETAIL, bundleOf(AnalyticParamTags.FORUM_NAME to forumName))
    }

    override fun onForumMessagesClick(forumId: String) {
        sendKeyValueEvent(AnalyticTags.FORUM_MESSAGES, bundleOf(AnalyticParamTags.FORUM_ID to forumId))
    }

    override fun onAnnouncementShowClick(listId: String) {
        sendKeyValueEvent(AnalyticTags.ANNOUNCEMENT_SHOW, bundleOf(AnalyticParamTags.ANNOUNCEMENT_LIST_ID to listId))
    }

    override fun onAnnouncementDetailsClick(id: Int) {
        sendKeyValueEvent(AnalyticTags.ANNOUNCEMENT_DETAIL, bundleOf(AnalyticParamTags.ANNOUNCEMENT_ID to id))
    }

    override fun onShareInfoClick(fromScreenName: String) {
        sendKeyValueEvent(AnalyticTags.SHARE_CLICK, bundleOf(AnalyticParamTags.SCREEN_NAME to fromScreenName))
    }

    override fun onShareInfoComplete(fromScreenName: String, appName: String) {
        sendKeyValueEvent(AnalyticTags.SHARE_COMPLETE,
                bundleOf(AnalyticParamTags.SCREEN_NAME to fromScreenName,
                        AnalyticParamTags.SOCIAL_APP_NAME to appName))
    }

    override fun onCopyTextClick(fromScreenName: String) {
        sendKeyValueEvent(AnalyticTags.COPY_TEXT_CLICK, bundleOf(AnalyticParamTags.SCREEN_NAME to fromScreenName))
    }
}