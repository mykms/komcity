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
    private var isSearchMode = false
    private var isHasItemsMore = true
    private var startDateTime = 0L
    private var endDateTime = 0L
    private val items = arrayListOf<NewsItem>()
    private val searchItems = arrayListOf<NewsItem>()
    private var searchPage = 1
    private var listPage = 1
    private var scrollPosition = 0

    fun getNewsList() {
        loadNews(listPage, startDateTime, endDateTime)
    }

    private fun loadNews(page: Int, startDate: Long, endDate: Long) {
        newsJob = CoroutineScope(getExceptionHandler { doOnError(it) }).launch {
            withContext(Dispatchers.Main) {
                viewState.onLoading(true)
            }
            withContext(Dispatchers.IO) {
                val repoItems = newsRepository.getNews(page, startDate, endDate)
                withContext(Dispatchers.Main) {
                    isHasItemsMore = !(repoItems.isEmpty() && startDate == 0L && endDate == 0L)
                    viewState.onNewsLoaded(checkCache(page, startDate, endDate, repoItems))
                    viewState.scrollTo(scrollPosition)
                    viewState.onLoading(false)
                }
            }
        }
    }

    private fun checkCache(page: Int, startDate: Long, endDate: Long, repoItems: List<NewsItem>): List<NewsItem> {
        return if (startDate == 0L && endDate == 0L) {
            items.apply {
                addAll(repoItems)
            }
        } else {
            searchItems.apply {
                addAll(repoItems)
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

    fun getNextPageNews(totalCount: Int, lastPosition: Int) {
        if (lastPosition == totalCount - 1 && isHasItemsMore) {
            val page = if (startDateTime == 0L && endDateTime == 0L) {
                ++listPage
            } else {
                ++searchPage
            }
            scrollPosition = lastPosition
            loadNews(page, startDateTime, endDateTime)
        }
    }

    fun navigateByItemType(item: BaseHolderItem) {
        when (item) {
            is AddNewsItem -> navigateTo(R.id.newsAdd, bundleOf())
            is NewsItem -> navigateTo(R.id.newsDetailFragment, bundleOf(
                    Constants.EXTRA_NEWS_ID to item.newsId,
                    Constants.EXTRA_TITLE to item.title))
            else -> {}
        }
    }

    fun navigateTo(screenId: Int, args: Bundle) {
        viewState.navigateToScreen(screenId, args)
    }

    fun onSearchDialogPositive(start: Long, end: Long) {
        isSearchMode = true
        startDateTime = start
        endDateTime = end
        searchPage = 1
        searchItems.clear()
        viewState.searchResetIsVisible(true)
        loadNews(searchPage, startDateTime, endDateTime)
    }

    fun onSearchDialogNegative() {
    }

    /**
     * Диалоговое окно с выбором даты закрылось
     */
    fun onSearchDialogClosed() {
    }

    fun onSearchReset() {
        isSearchMode = false
        isHasItemsMore = true
        startDateTime = 0L
        endDateTime = 0L
        searchPage = 1
        listPage = 1
        searchItems.clear()
        items.clear()
        viewState.searchResetIsVisible(false)
        loadNews(listPage, startDateTime, endDateTime)
    }

    fun onSearchClick() {
        viewState.showSearchDialog()
    }

    override fun onDestroy() {
        newsJob?.cancel()
        super.onDestroy()
    }
}