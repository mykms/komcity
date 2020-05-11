package ru.komcity.mobile.presenter

import android.os.Bundle
import androidx.core.os.bundleOf
import kotlinx.coroutines.*
import moxy.InjectViewState
import ru.komcity.mobile.R
import ru.komcity.mobile.repository.ForumRepository
import ru.komcity.mobile.view.ForumView
import ru.komcity.mobile.viewModel.ForumItem
import ru.komcity.mobile.viewModel.ForumMessagesItem
import ru.komcity.mobile.viewModel.SubForumItem
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*

@InjectViewState
class ForumPresenter constructor(private val forumRepository: ForumRepository): BasePresenter<ForumView>() {

    private var forumJob: Job? = null
    private var title = ""
    private var forumName = ""
    private var subForumId = ""
    private val hideSocialTimer = Timer()

    fun initSubForumState(title: String, forumName: String) {
        this.title = title
        this.forumName = forumName
        viewState.setToolbarTitle(title)
    }

    fun initForumMessagesState(title: String, forumName: String, subForumId: String) {
        this.title = title
        this.forumName = forumName
        this.subForumId = subForumId
        viewState.setToolbarTitle(title)
    }

    fun getForums() {
        forumJob = CoroutineScope(getExceptionHandler { doOnError(it) }).launch {
            viewState.onLoading(true)
            withContext(Dispatchers.IO) {
                val items = forumRepository.getForums().map {
                    with(it) {
                        ForumItem(forumName, description, countReplic, countTheme, linkForum)
                    }
                }
                withContext(Dispatchers.Main) {
                    viewState.onForumList(items)
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
            else -> {
                viewState.onError("Произошла ошибка, попробуйте позже")
            }
        }
    }

    fun navigateTo(screenId: Int, args: Bundle) {
        viewState.navigateToScreen(screenId, args)
    }

    fun getSubForum() {
        forumJob = CoroutineScope(getExceptionHandler { doOnError(it) }).launch {
            viewState.onLoading(true)
            withContext(Dispatchers.IO) {
                val items = forumRepository.getSubForums(forumName).map {
                    with(it) {
                        SubForumItem(forumName, dateTime, countReplic, countTheme, linkForum)
                    }
                }
                withContext(Dispatchers.Main) {
                    viewState.onSubForumList(items, forumName)
                    viewState.onLoading(false)
                }
            }
        }
    }

    fun getForumMessages() {
        forumJob = CoroutineScope(getExceptionHandler { doOnError(it) }).launch {
            viewState.onLoading(true)
            withContext(Dispatchers.IO) {
                val id = subForumId.toIntOrNull() ?: 0
                val items = forumRepository.getForumMessages(forumName, id).map {
                    with(it) {
                        ForumMessagesItem(date, userName, message)
                    }
                }
                withContext(Dispatchers.Main) {
                    viewState.onForumMessages(items)
                    viewState.onLoading(false)
                }
            }
        }
    }

    fun navigateToBackScreen() {
        viewState.navigateToBackScreen()
    }

    fun onForumMessageAction(text: String, isShare: Boolean) {
        if (isShare) {
            viewState.onCopyText(text)
        } else {
            viewState.onShareText(text)
        }
    }

    override fun onDestroy() {
        forumJob?.cancel()
        super.onDestroy()
    }
}