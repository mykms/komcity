package ru.komcity.mobile.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.komcity.mobile.viewModel.ForumItem
import ru.komcity.mobile.viewModel.ForumMessagesItem
import ru.komcity.mobile.viewModel.SubForumItem

interface ForumView : MvpView {
    @StateStrategyType(AddToEndStrategy::class) fun onLoading(isLoading: Boolean)
    @StateStrategyType(AddToEndStrategy::class) fun onForumList(items: List<ForumItem>)
    @StateStrategyType(AddToEndStrategy::class) fun onSubForumList(items: List<SubForumItem>)
    @StateStrategyType(AddToEndStrategy::class) fun onForumMessages(items: List<ForumMessagesItem>)
}