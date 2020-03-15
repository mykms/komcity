package ru.komcity.mobile.presenter

import kotlinx.coroutines.*
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

    private var filterJob: Job? = null

    fun init() {
    }

    fun getFilters() {
        viewState.onLoading(true)
        filterJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                val items = repository.getAnnouncementFilters()
                withContext(Dispatchers.Main) {
                    viewState.onLoading(false)
                    viewState.onAnnouncementCategory(items)
                }
            } catch (ex: Exception) {
                viewState.onLoading(false)
                ex.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        filterJob?.cancel()
        super.onDestroy()
    }
}