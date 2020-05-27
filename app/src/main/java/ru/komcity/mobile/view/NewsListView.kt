package ru.komcity.mobile.view

import android.os.Bundle
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.komcity.mobile.viewModel.NewsItem

interface NewsListView: MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class) fun onLoading(isLoading: Boolean)
    @StateStrategyType(OneExecutionStateStrategy::class) fun onError(message: String)
    @StateStrategyType(OneExecutionStateStrategy::class) fun onNewsLoaded(items: List<NewsItem>)
    @StateStrategyType(OneExecutionStateStrategy::class) fun scrollTo(position: Int)
    @StateStrategyType(OneExecutionStateStrategy::class) fun navigateToScreen(screenId: Int, args: Bundle)
    @StateStrategyType(OneExecutionStateStrategy::class) fun showSearchDialog()
    @StateStrategyType(OneExecutionStateStrategy::class) fun hideSearchDialog()
    @StateStrategyType(OneExecutionStateStrategy::class) fun searchResetIsVisible(isVisible: Boolean)
}