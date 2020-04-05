package ru.komcity.mobile.view

import android.os.Bundle
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * Created by Aleksey on 2020.03.01
 * <p>
 * View for Filtering Announcements screen
 */
interface AnnouncementsFilterView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class) fun onLoading(isLoading: Boolean)
    @StateStrategyType(OneExecutionStateStrategy::class) fun navigateToScreen(screenId: Int, args: Bundle)
    @StateStrategyType(OneExecutionStateStrategy::class) fun navigateToBackScreen()
    @StateStrategyType(OneExecutionStateStrategy::class) fun setCategoryTitle(text: String, isCloseVisible: Boolean)
    @StateStrategyType(OneExecutionStateStrategy::class) fun setSubCategoryCategoryTitle(text: String, isCloseVisible: Boolean)
    @StateStrategyType(OneExecutionStateStrategy::class) fun showCategoryDialog(items: List<String>)
    @StateStrategyType(OneExecutionStateStrategy::class) fun showSubCategoryDialog(items: List<String>)
}