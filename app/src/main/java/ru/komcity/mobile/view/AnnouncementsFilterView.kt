package ru.komcity.mobile.view

import android.os.Bundle
import ru.komcity.mobile.presenter.MvpView

/**
 * Created by Aleksey on 2020.03.01
 * <p>
 * View for Filtering Announcements screen
 */
interface AnnouncementsFilterView : MvpView {
    fun onLoading(isLoading: Boolean)
    fun navigateToScreen(screenId: Int, args: Bundle)
    fun onError(message: String)
    fun showMessage(message: String)
    fun navigateToBackScreen()
    fun setCategoryTitle(text: String, isCloseVisible: Boolean)
    fun setCategoryVisibility(isVisible: Boolean)
    fun setSubCategoryCategoryTitle(text: String, isCloseVisible: Boolean)
    fun setSubCategoryVisibility(isVisible: Boolean)
    fun setDetailCategoryTitle(text: String, isCloseVisible: Boolean)
    fun setDetailCategoryVisibility(isVisible: Boolean)
    fun setDetailSubCategoryCategoryTitle(text: String, isCloseVisible: Boolean)
    fun setDetailSubCategoryVisibility(isVisible: Boolean)
    fun showCategoryDialog(items: List<String>)
    fun showSubCategoryDialog(items: List<String>)
    fun showDetailCategoryDialog(items: List<String>)
    fun showDetailSubCategoryDialog(items: List<String>)
    fun onShowClick(listId: String)
}