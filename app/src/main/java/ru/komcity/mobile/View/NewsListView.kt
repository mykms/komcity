package ru.komcity.mobile.View

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.komcity.mobile.Model.NewsItem

interface NewsListView: MvpView {
    @StateStrategyType(AddToEndStrategy::class)
    fun onLoadingStart()
    @StateStrategyType(AddToEndStrategy::class)
    fun onLoadingStop()
    @StateStrategyType(AddToEndStrategy::class)
    fun onNewsLoaded(items: List<NewsItem>)
}