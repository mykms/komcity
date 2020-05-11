package ru.komcity.mobile.view

import android.os.Bundle
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.komcity.mobile.viewModel.ForumItem
import ru.komcity.mobile.viewModel.ForumMessagesItem
import ru.komcity.mobile.viewModel.SubForumItem

interface ForumView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class) fun onLoading(isLoading: Boolean)
    @StateStrategyType(OneExecutionStateStrategy::class) fun onError(message: String)
    @StateStrategyType(OneExecutionStateStrategy::class) fun navigateToScreen(screenId: Int, args: Bundle)
    @StateStrategyType(OneExecutionStateStrategy::class) fun navigateToBackScreen()
    @StateStrategyType(OneExecutionStateStrategy::class) fun setToolbarTitle(title: String)
    @StateStrategyType(OneExecutionStateStrategy::class) fun onForumList(items: List<ForumItem>)
    @StateStrategyType(OneExecutionStateStrategy::class) fun onSubForumList(items: List<SubForumItem>, forumName: String)
    @StateStrategyType(OneExecutionStateStrategy::class) fun onForumMessages(items: List<ForumMessagesItem>)
    @StateStrategyType(OneExecutionStateStrategy::class) fun onCopyText(text: String)
    @StateStrategyType(OneExecutionStateStrategy::class) fun onShareText(text: String)
}