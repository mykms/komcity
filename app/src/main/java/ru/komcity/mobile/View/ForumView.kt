package ru.komcity.mobile.View

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.komcity.mobile.Model.ForumItem

interface ForumView : MvpView {
    @StateStrategyType(AddToEndStrategy::class)
    fun onLoadingStart()
    @StateStrategyType(AddToEndStrategy::class)
    fun onLoadingStop()
    @StateStrategyType(AddToEndStrategy::class)
    fun onForumList(items: List<ForumItem>)
    @StateStrategyType(AddToEndStrategy::class)
    fun onSubForumList(items: List<ForumItem>)
    @StateStrategyType(AddToEndStrategy::class)
    fun onForumDetail(item: ForumItem)
}