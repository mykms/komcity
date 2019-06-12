package ru.komcity.mobile.View

import com.arellomobile.mvp.MvpView
import ru.komcity.mobile.Model.ForumItem

interface ForumView : MvpView {
    fun onLoadingStart()
    fun onLoadingStop()

    fun onForumList(items: List<ForumItem>)
    fun onSubForumList(items: List<ForumItem>)
    fun onForumDetail(item: ForumItem)
}