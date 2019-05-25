package ru.komcity.mobile.Presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.komcity.mobile.View.NewsListView

@InjectViewState
class NewsPresenter: MvpPresenter<NewsListView>() {

    fun getNews() {
        viewState.onNewsLoaded(listOf())
    }
}