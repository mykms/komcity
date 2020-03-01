package ru.komcity.mobile.presenter

import moxy.InjectViewState
import ru.komcity.mobile.repository.AnnouncementsFilterRepository
import ru.komcity.mobile.view.AnnouncementsFilterView

/**
 * Created by Aleksey on 2020.03.01
 * <p>
 * Presenter for Filtering Announcements screen
 */
@InjectViewState
class AnnouncementsFilterPresenter constructor(private val repository: AnnouncementsFilterRepository)
    : BasePresenter<AnnouncementsFilterView>() {
    //
}