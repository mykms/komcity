package ru.komcity.mobile.view

import ru.komcity.mobile.network.MailSenderData
import ru.komcity.mobile.presenter.MvpView
import ru.komcity.mobile.viewModel.addnews.AddNewsBaseItem

/**
 * Created by Aleksei Kholoimov on 07.05.2020
 * <p>
 * View для экрана добавления новости
 */
interface NewsAddView: MvpView {
    fun onLoading(isLoading: Boolean)
    fun onError(message: String)
    fun onParamsLoaded(item: MailSenderData)
    fun navigateToBackScreen()
    fun onFileLoadComplete(items: List<AddNewsBaseItem>)
}