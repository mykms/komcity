package ru.komcity.mobile.presenter

import moxy.InjectViewState
import ru.komcity.mobile.network.MailSender
import ru.komcity.mobile.network.MailSenderData
import ru.komcity.mobile.repository.SendRepository
import ru.komcity.mobile.view.NewsAddView
import ru.komcity.mobile.viewModel.addnews.AddNewsItemEmpty

/**
 * Created by Aleksei Kholoimov on 07.05.2020
 * <p>
 * Презентер добавления новой новости
 */
@InjectViewState
class NewsAddPresenter constructor(private val sendRepository: SendRepository) : BasePresenter<NewsAddView>() {

    private var mailSender: MailSender? = null

    fun getSendParams() {
        //sendRepository
        val mailData = MailSenderData(
                emailFrom = "test201701@yandex.ru",
                userLogin = "test201701",
                userPwd = "123456789q",
                serverHost = "smtp.yandex.ru",
                serverPort = "456")
        mailSender = MailSender(mailData)
        viewState.onParamsLoaded(mailData)
        viewState.onFileLoadComplete(listOf(AddNewsItemEmpty()))
    }

    fun checkAndSendNews(subject: String, description: String) {
        val theme = subject.trim()
        if (description.trim().isEmpty()) {
            viewState.onError("Поле с описанием не должно быть пустым")
        } else {
            viewState.onLoading(true)
            mailSender?.sendMail(theme, description) {
                isSuccess, message ->
                viewState.onLoading(false)
                if (!isSuccess) {
                    viewState.onError(message)
                } else {
                    viewState.onError("Новость успешно отправлена!")
                    viewState.navigateToBackScreen()
                }
            }
        }
    }

    fun onAttachFileClick(isEditFile: Boolean) {
        if (isEditFile) {
            // delete file dialog
        } else {
            // shod add dialog
        }
    }

    fun navigateToBackScreen() {
        viewState.navigateToBackScreen()
    }
}