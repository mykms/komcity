package ru.komcity.mobile.view

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * Created by Aleksey on 2020.03.01
 * <p>
 * View for Filtering Announcements screen
 */
interface AnnouncementsFilterView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onLoading(isLoading: Boolean)
}