package ru.komcity.mobile.view

import android.os.Bundle
import ru.komcity.mobile.presenter.MvpView
import ru.komcity.mobile.viewModel.NewsItem

interface NewsListView: MvpView {
    fun onLoading(isLoading: Boolean)
    fun onError(message: String)
    fun onNewsLoaded(items: List<NewsItem>)
    fun scrollTo(position: Int)
    fun navigateToScreen(screenId: Int, args: Bundle)
    fun showSearchDialog()
    fun hideSearchDialog()
    fun searchResetIsVisible(isVisible: Boolean)
}