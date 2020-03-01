package ru.komcity.mobile.presenter

import moxy.InjectViewState
import ru.komcity.mobile.repository.AnnouncementsRepository
import ru.komcity.mobile.view.AnnouncementsView

/**
 * Created by Aleksey on 2020.03.01
 * <p>
 * Presenter for Announcements screen
 */
@InjectViewState
class AnnouncementsPresenter constructor(private val repository: AnnouncementsRepository)
    : BasePresenter<AnnouncementsView>() {

    fun getAnnouncements() {
        //repository
    }
}