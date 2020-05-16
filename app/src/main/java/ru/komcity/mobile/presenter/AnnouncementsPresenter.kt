package ru.komcity.mobile.presenter

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.komcity.mobile.repository.AnnouncementsRepository
import ru.komcity.mobile.view.AnnouncementsView
import ru.komcity.mobile.viewModel.Announcement

/**
 * Created by Aleksey on 2020.03.01
 * <p>
 * Presenter for Announcements screen
 */
@InjectViewState
class AnnouncementsPresenter constructor(private val repository: AnnouncementsRepository)
    : BasePresenter<AnnouncementsView>() {

    private var announcementJob: Job? = null
    private var id = 0
    private val items = arrayListOf<Announcement>()

    fun initState(id: String) {
        this.id = id.removePrefix("r").toIntOrNull() ?: 0
    }

    fun getAnnouncements() {
        viewState.onLoading(true)
        announcementJob = CoroutineScope(Dispatchers.Main).launch {
            repository.getAnnouncements(id)
                    .onStart {
                        items.clear()
                    }
                    .catch {
                        viewState.onLoading(false)
                        it.printStackTrace()
                    }
                    .map {
                        items.add(it)
                    }
                    .onCompletion {
                        viewState.onLoading(false)
                        viewState.showAnnouncements(items)
                    }
                    .collect {
                        val x = 0
                    }
        }
    }

    fun navigateToBackScreen() {
        viewState.navigateToBackScreen()
    }

    override fun onDestroy() {
        announcementJob?.cancel()
        super.onDestroy()
    }
}