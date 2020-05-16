package ru.komcity.mobile.view

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.komcity.mobile.viewModel.Announcement

/**
 * Created by Aleksey on 2020.03.01
 * <p>
 * View for Announcements screen
 */
interface AnnouncementsView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class) fun onLoading(isLoading: Boolean)
    @StateStrategyType(OneExecutionStateStrategy::class) fun navigateToBackScreen()
    @StateStrategyType(OneExecutionStateStrategy::class) fun showAnnouncements(items: List<Announcement>)
}