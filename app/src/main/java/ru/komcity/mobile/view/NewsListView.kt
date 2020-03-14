package ru.komcity.mobile.view

import android.os.Bundle
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.komcity.mobile.viewModel.NewsItem

interface NewsListView: MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onLoading(isLoading: Boolean)
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onNewsLoaded(items: List<NewsItem>)
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToScreen(screenId: Int, args: Bundle)
}