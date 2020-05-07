package ru.komcity.mobile.presenter

import android.os.Bundle
import androidx.core.os.bundleOf
import kotlinx.coroutines.*
import moxy.InjectViewState
import ru.komcity.mobile.R
import ru.komcity.mobile.common.Constants
import ru.komcity.mobile.repository.NewsRepository
import ru.komcity.mobile.view.NewsListView
import ru.komcity.mobile.viewModel.AddNewsItem
import ru.komcity.mobile.viewModel.BaseHolderItem
import ru.komcity.mobile.viewModel.NewsItem
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@InjectViewState
class NewsPresenter constructor(private val newsRepository: NewsRepository): BasePresenter<NewsListView>() {

    private var newsJob: Job? = null

    fun getNewsList() {
        newsJob = CoroutineScope(getExceptionHandler { doOnError(it) }).launch {
            viewState.onLoading(true)
            withContext(Dispatchers.IO) {
                val items = newsRepository.getNews()
                withContext(Dispatchers.Main) {
                    viewState.onNewsLoaded(items)
                    viewState.onLoading(false)
                }
            }
        }
    }

    private fun doOnError(throwable: Throwable) {
        viewState.onLoading(false)
        when (throwable) {
            is ConnectException -> {
                viewState.onError("Не удается соединиться с сервером")
            }
            is UnknownHostException -> {
                viewState.onError("Проверьте связь с интернетом или адрес сервера")
            }
            is IllegalArgumentException -> {
                viewState.onError("Произошла ошибка, попробуйте позже")
            }
            is SocketTimeoutException -> {
                viewState.onError("Проверьте связь с интернетом и попробуйте позже")
            }
            else -> {}
        }
    }

    fun navigateByItemType(item: BaseHolderItem) {
        when (item) {
            is AddNewsItem -> navigateTo(R.id.newsAdd, bundleOf())
            is NewsItem -> navigateTo(R.id.newsDetailFragment, bundleOf(
                    Constants.EXTRA_NEWS_ID to item.newsId,
                    Constants.EXTRA_NEWS_TITLE to item.title))
            else -> {}
        }
    }

    fun navigateTo(screenId: Int, args: Bundle) {
        viewState.navigateToScreen(screenId, args)
    }

    override fun onDestroy() {
        newsJob?.cancel()
        super.onDestroy()
    }
}