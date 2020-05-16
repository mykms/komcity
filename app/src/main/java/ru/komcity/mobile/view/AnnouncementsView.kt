package ru.komcity.mobile.view

import android.os.Bundle
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.komcity.mobile.viewModel.Announcement

/**
 * Created by Aleksey on 2020.03.01
 * <p>
 * View for Announcements screen
 */
interface AnnouncementsView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class) fun onLoading(isLoading: Boolean)
    @StateStrategyType(SkipStrategy::class) fun navigateToScreen(screenId: Int, args: Bundle)
    @StateStrategyType(OneExecutionStateStrategy::class) fun onError(message: String)
    @StateStrategyType(OneExecutionStateStrategy::class) fun navigateToBackScreen()
    @StateStrategyType(OneExecutionStateStrategy::class) fun showAnnouncements(items: List<Announcement>)
}