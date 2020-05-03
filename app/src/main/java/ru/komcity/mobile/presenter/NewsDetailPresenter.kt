package ru.komcity.mobile.presenter

import android.os.Bundle
import kotlinx.coroutines.*
import moxy.InjectViewState
import ru.komcity.mobile.repository.NewsRepository
import ru.komcity.mobile.view.NewsDetailView
import java.net.UnknownHostException

/**
 * Created by Aleksei Kholoimov on 14.03.2020
 * <p>
 * Presenter for screen news detail info
 */
@InjectViewState
class NewsDetailPresenter constructor(private val newsRepository: NewsRepository) : BasePresenter<NewsDetailView>() {

    private var newsJob: Job? = null
    private var newsId = 0
    private var isVisibleSharePanel = false
    private var title: String = ""

    fun init(newsId: Int, title: String) {
        this.newsId = newsId
        this.title = title
    }

    fun iniState() {
        viewState.setToolbarTitle(title)
        viewState.setVisibilitySharePanel(isVisibleSharePanel)
    }

    fun getNewsDetail() {
        viewState.onLoading(true)
        newsJob = CoroutineScope(getExceptionHandler { doOnError(it) }).launch {
            withContext(Dispatchers.IO) {
                val item = newsRepository.getNewsDetail(newsId)
                withContext(Dispatchers.Main) {
                    viewState.onNewsDetailLoaded(item)
                    viewState.onLoading(false)
                }
            }
        }
    }

    private fun doOnError(throwable: Throwable) {
        viewState.onLoading(false)
        when (throwable) {
            is UnknownHostException -> {
                //viewState.onError("Проверьте связь с интернетом или адрес сервера")
            }
            is IllegalArgumentException -> {
                //viewState.onError("Произошла ошибка, попробуйте позже")
            }
            else -> {}
        }
    }

    fun navigateTo(screenId: Int, args: Bundle) {
        viewState.navigateToScreen(screenId, args)
    }

    fun navigateToBackScreen() {
        viewState.navigateToBackScreen()
    }

    fun changeVisibilitySharePanel() {
        isVisibleSharePanel = !isVisibleSharePanel
        viewState.setVisibilitySharePanel(isVisibleSharePanel)
    }

    override fun onDestroy() {
        newsJob?.cancel()
        super.onDestroy()
    }
}