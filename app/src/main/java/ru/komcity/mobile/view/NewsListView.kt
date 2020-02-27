package ru.komcity.mobile.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.komcity.mobile.viewModel.NewsItem

interface NewsListView: MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onLoading(isLoading: Boolean)
    @StateStrategyType(AddToEndStrategy::class)
    fun onNewsLoaded(items: List<NewsItem>)
}