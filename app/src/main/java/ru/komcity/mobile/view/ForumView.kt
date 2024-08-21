package ru.komcity.mobile.view

import android.os.Bundle
import com.sharetosocial.android.SocialApp
import ru.komcity.mobile.presenter.MvpView
import ru.komcity.mobile.viewModel.ForumItem
import ru.komcity.mobile.viewModel.ForumMessagesItem
import ru.komcity.mobile.viewModel.SubForumItem

interface ForumView : MvpView {
    fun onLoading(isLoading: Boolean)
    fun onError(message: String)
    fun navigateToScreen(screenId: Int, args: Bundle)
    fun navigateToBackScreen()
    fun setToolbarTitle(title: String)
    fun onForumList(items: List<ForumItem>)
    fun onSubForumList(items: List<SubForumItem>, forumName: String)
    fun onForumMessages(items: List<ForumMessagesItem>)
    fun onCopyText(text: String)
    fun showSocial()
    fun hideSocial()
    fun onShareSocial(item: SocialApp, description: String)
}