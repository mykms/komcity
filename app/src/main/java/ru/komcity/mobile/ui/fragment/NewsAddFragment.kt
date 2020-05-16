package ru.komcity.mobile.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_news_add.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.komcity.mobile.R
import ru.komcity.mobile.common.analytic.AnalyticManager
import ru.komcity.mobile.common.analytic.AnalyticManagerImpl
import ru.komcity.mobile.network.ApiNetwork
import ru.komcity.mobile.network.MailSenderData
import ru.komcity.mobile.presenter.NewsAddPresenter
import ru.komcity.mobile.repository.SendRepositoryImpl
import ru.komcity.mobile.ui.adapter.NewsAddFileAdapter
import ru.komcity.mobile.view.NewsAddView
import ru.komcity.mobile.viewModel.addnews.AddNewsBaseItem

/**
 * Created by Aleksei Kholoimov on 07.05.2020
 * <p>
 * Добавление новой новости
 */
class NewsAddFragment: BaseFragment(), NewsAddView {

    private val api = ApiNetwork().api
    private val repo = SendRepositoryImpl(api)
    @InjectPresenter
    lateinit var newsPresenter: NewsAddPresenter
    @ProvidePresenter
    fun providePresenter() = NewsAddPresenter(repo)
    private lateinit var analytics: AnalyticManager

    override fun onCreateInit(clientId: String, context: Context) {
        analytics = AnalyticManagerImpl(clientId, context)
    }

    override fun getArgs(args: Bundle?) {
    }

    override fun setResourceLayout(): Int = R.layout.fragment_news_add

    override fun initComponents(view: View) {
        progress.isVisible = false
        setBottomPanelVisibility(false)
        initToolbar()
        initRecyclerView()
        btSend.setOnClickListener {
            hideKeyboard()
            newsPresenter.checkAndSendNews(etSubject.text.toString(), etDescription.text.toString())
        }
        newsPresenter.getSendParams()
    }

    private fun initToolbar() = with(toolbar) {
        title = "Предложить новость"
        setNavigationIcon(R.drawable.vector_ic_arrow_back_white)
        setNavigationOnClickListener {
            newsPresenter.navigateToBackScreen()
        }
    }

    private fun initRecyclerView() = with(rvAttaches) {
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = null
    }

    override fun onLoading(isLoading: Boolean) {
        progress.isVisible = isLoading
    }

    override fun onError(message: String) {
        onMessage(message)
    }

    override fun onParamsLoaded(item: MailSenderData) {
    }

    override fun navigateToBackScreen() {
        hideKeyboard()
        navigateToBack()
    }

    override fun onFileLoadComplete(items: List<AddNewsBaseItem>) {
        rvAttaches.adapter = NewsAddFileAdapter(items) {
            newsPresenter.onAttachFileClick(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        setBottomPanelVisibility(true)
    }
}