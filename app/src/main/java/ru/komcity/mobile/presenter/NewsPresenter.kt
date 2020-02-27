package ru.komcity.mobile.presenter

import kotlinx.coroutines.*
import moxy.InjectViewState
import ru.komcity.mobile.repository.NewsRepository
import ru.komcity.mobile.view.NewsListView
import ru.komcity.mobile.viewModel.NewsItem

@InjectViewState
class NewsPresenter constructor(private val newsRepository: NewsRepository): BasePresenter<NewsListView>() {

    private var newsJob: Job? = null

    fun getNewsList() {
        viewState.onLoading(true)
        newsJob = CoroutineScope(Dispatchers.IO).launch {
            val items = newsRepository.getNews().map {
                with(it) {
                    NewsItem(title, date, shortText, previewImg, imageUrls, newsId.toIntOrNull() ?: 0)
                }
            }
            withContext(Dispatchers.Main) {
                viewState.onLoading(false)
                viewState.onNewsLoaded(items)
            }
        }
    }

    override fun onDestroy() {
        newsJob?.cancel()
        super.onDestroy()
    }
}