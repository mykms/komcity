package ru.komcity.mobile.view

import android.os.Bundle
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.komcity.mobile.viewModel.NewsItem

/**
 * Created by Aleksei Kholoimov on 14.03.2020
 * <p>
 * View for screen news detail info
 */
interface NewsDetailView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class) fun onLoading(isLoading: Boolean)
    @StateStrategyType(OneExecutionStateStrategy::class) fun onNewsDetailLoaded(item: NewsItem)
    @StateStrategyType(OneExecutionStateStrategy::class) fun navigateToScreen(screenId: Int, args: Bundle)
    @StateStrategyType(OneExecutionStateStrategy::class) fun navigateToBackScreen()
    @StateStrategyType(OneExecutionStateStrategy::class) fun setToolbarTitle(title: String)
    @StateStrategyType(OneExecutionStateStrategy::class) fun setVisibilitySharePanel(isVisible: Boolean)
}