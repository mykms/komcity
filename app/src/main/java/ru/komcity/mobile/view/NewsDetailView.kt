package ru.komcity.mobile.view

import android.os.Bundle
import com.sharetosocial.android.SocialApp
import ru.komcity.mobile.presenter.MvpView
import ru.komcity.mobile.viewModel.NewsItem
import java.io.File

/**
 * Created by Aleksei Kholoimov on 14.03.2020
 * <p>
 * View for screen news detail info
 */
interface NewsDetailView : MvpView {
    fun onLoading(isLoading: Boolean)
    fun onError(message: String)
    fun onNewsDetailLoaded(item: NewsItem)
    fun navigateToScreen(screenId: Int, args: Bundle)
    fun navigateToBackScreen()
    fun setToolbarTitle(title: String)
    fun setVisibilitySharePanel(isVisible: Boolean)
    fun checkStoragePermissions()
    fun onShareSocial(item: SocialApp, position: Int)
    fun onSaveMediaSuccess(item: SocialApp, file: File)
}