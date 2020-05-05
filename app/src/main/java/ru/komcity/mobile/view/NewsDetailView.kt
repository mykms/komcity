package ru.komcity.mobile.view

import android.os.Bundle
import com.sharetosocial.android.SocialApp
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.komcity.mobile.viewModel.NewsItem
import java.io.File

/**
 * Created by Aleksei Kholoimov on 14.03.2020
 * <p>
 * View for screen news detail info
 */
interface NewsDetailView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class) fun onLoading(isLoading: Boolean)
    @StateStrategyType(OneExecutionStateStrategy::class) fun onError(message: String)
    @StateStrategyType(OneExecutionStateStrategy::class) fun onNewsDetailLoaded(item: NewsItem)
    @StateStrategyType(OneExecutionStateStrategy::class) fun navigateToScreen(screenId: Int, args: Bundle)
    @StateStrategyType(OneExecutionStateStrategy::class) fun navigateToBackScreen()
    @StateStrategyType(OneExecutionStateStrategy::class) fun setToolbarTitle(title: String)
    @StateStrategyType(OneExecutionStateStrategy::class) fun setVisibilitySharePanel(isVisible: Boolean)
    @StateStrategyType(OneExecutionStateStrategy::class) fun checkStoragePermissions()
    @StateStrategyType(OneExecutionStateStrategy::class) fun onShareSocial(item: SocialApp, position: Int)
    @StateStrategyType(OneExecutionStateStrategy::class) fun onSaveMediaSuccess(item: SocialApp, file: File)
}