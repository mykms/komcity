package ru.komcity.mobile.view

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.komcity.mobile.network.MailSenderData

/**
 * Created by Aleksei Kholoimov on 07.05.2020
 * <p>
 * View для экрана добавления новости
 */
interface NewsAddView: MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class) fun onLoading(isLoading: Boolean)
    @StateStrategyType(OneExecutionStateStrategy::class) fun onError(message: String)
    @StateStrategyType(OneExecutionStateStrategy::class) fun onParamsLoaded(item: MailSenderData)
    @StateStrategyType(OneExecutionStateStrategy::class) fun navigateToBackScreen()
}