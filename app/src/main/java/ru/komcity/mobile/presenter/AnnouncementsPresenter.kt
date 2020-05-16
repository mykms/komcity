package ru.komcity.mobile.presenter

import android.os.Bundle
import androidx.core.os.bundleOf
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import moxy.InjectViewState
import ru.komcity.mobile.R
import ru.komcity.mobile.repository.AnnouncementsRepository
import ru.komcity.mobile.view.AnnouncementsView
import ru.komcity.mobile.viewModel.Announcement
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

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
        items.clear()
        announcementJob = CoroutineScope(getExceptionHandler { doOnError(it) }).launch {
            withContext(Dispatchers.IO) {
                val item = repository.getAnnouncements(id).map {
                    items.add(it)
                }
                item.collect()
                withContext(Dispatchers.Main) {
                    viewState.showAnnouncements(items)
                    viewState.onLoading(false)
                }
            }
        }
    }

    private fun doOnError(throwable: Throwable) {
        viewState.onLoading(false)
        when (throwable) {
            is ConnectException -> {
                //viewState.onError("Не удается соединиться с сервером")
                navigateTo(R.id.connectionErrorFragment, bundleOf())
            }
            is UnknownHostException -> {
                //viewState.onError("Проверьте связь с интернетом или адрес сервера")
                navigateTo(R.id.connectionErrorFragment, bundleOf())
            }
            is IllegalArgumentException -> {
                viewState.onError("Произошла ошибка, попробуйте позже")
            }
            is SocketTimeoutException -> {
                //viewState.onError("Проверьте связь с интернетом и попробуйте позже")
                navigateTo(R.id.connectionErrorFragment, bundleOf())
            }
            else -> {}
        }
    }

    private fun navigateTo(screenId: Int, args: Bundle) {
        viewState.navigateToScreen(screenId, args)
    }

    fun navigateToBackScreen() {
        viewState.navigateToBackScreen()
    }

    override fun onDestroy() {
        announcementJob?.cancel()
        super.onDestroy()
    }
}