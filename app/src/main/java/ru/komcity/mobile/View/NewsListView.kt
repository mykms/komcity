package ru.komcity.mobile.View

import com.arellomobile.mvp.MvpView
import ru.komcity.mobile.Model.NewsItem

interface NewsListView: MvpView {

    fun onNewsLoaded(items: List<NewsItem>)
}