package ru.komcity.mobile.presenter

import android.os.Bundle
import kotlinx.coroutines.*
import moxy.InjectViewState
import ru.komcity.mobile.repository.NewsRepository
import ru.komcity.mobile.view.NewsDetailView
import ru.komcity.mobile.viewModel.NewsItem

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
    private lateinit var news: NewsItem

    fun init(news: NewsItem) {
        this.newsId = news.newsId
        this.news = news
    }

    fun iniState() {
        viewState.setToolbarTitle(news.title)
        viewState.setVisibilitySharePanel(isVisibleSharePanel)
    }

    fun getNewsDetail() {
        viewState.onLoading(true)
        newsJob = CoroutineScope(Dispatchers.IO).launch {
            try {
//                val item = newsRepository.getNewsDetail(newsId)
//                val newsDetail = with(item) {
//                    NewsItem(title, date, shortText, previewImg, imageUrls, this.newsId.toIntOrNull() ?: 0)
//                }
                withContext(Dispatchers.Main) {
                    viewState.onLoading(false)
                    viewState.onNewsDetailLoaded(news)
                }
            } catch (ex: Exception) {
                viewState.onLoading(false)
                ex.printStackTrace()
            }
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