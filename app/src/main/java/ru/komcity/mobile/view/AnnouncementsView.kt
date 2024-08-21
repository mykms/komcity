package ru.komcity.mobile.view

import android.os.Bundle
import ru.komcity.mobile.presenter.MvpView
import ru.komcity.mobile.viewModel.Announcement

/**
 * Created by Aleksey on 2020.03.01
 * <p>
 * View for Announcements screen
 */
interface AnnouncementsView : MvpView {
    fun onLoading(isLoading: Boolean)
    fun navigateToScreen(screenId: Int, args: Bundle)
    fun onError(message: String)
    fun navigateToBackScreen()
    fun showAnnouncements(items: List<Announcement>)
}