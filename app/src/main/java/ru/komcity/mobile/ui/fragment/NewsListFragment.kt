package ru.komcity.mobile.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_news_list.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.komcity.mobile.R
import ru.komcity.mobile.network.ApiNetwork
import ru.komcity.mobile.presenter.NewsPresenter
import ru.komcity.mobile.repository.NewsRepositoryImpl
import ru.komcity.mobile.ui.adapter.NewsAdapter
import ru.komcity.mobile.view.NewsListView
import ru.komcity.mobile.viewModel.AddNewsItem
import ru.komcity.mobile.viewModel.BaseHolderItem
import ru.komcity.mobile.viewModel.NewsItem
import ru.komcity.mobile.viewModel.SearchNewsItem
import ru.komcity.uicomponent.DividerWithRemoveDecorator

class NewsListFragment: BaseFragment(), NewsListView {

    private val api = ApiNetwork().api
    private val repo = NewsRepositoryImpl(api)
    @InjectPresenter
    lateinit var newsPresenter: NewsPresenter
    @ProvidePresenter
    fun providePresenter() = NewsPresenter(repo)
    private val searchAndAddNewsItems = listOf(SearchNewsItem(), AddNewsItem())

    override fun getArgs(args: Bundle?) {
        //newsPresenter.attachView(this)
    }

    override fun setResourceLayout(): Int {
        return R.layout.fragment_news_list
    }

    override fun initComponents(view: View) {
        initRecyclerView(view)
        newsPresenter.getNewsList()
    }

    private fun initRecyclerView(view: View) = with(rvListNews) {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        addItemDecoration(DividerWithRemoveDecorator(context, R.drawable.recycler_divider, 2, 0))
    }

    override fun onLoading(isLoading: Boolean) {
        progress.isVisible = isLoading
    }

    override fun onError(message: String) {
        onMessage(message)
    }

    override fun onNewsLoaded(items: List<NewsItem>) {
        val totalItems = arrayListOf<BaseHolderItem>().apply {
            addAll(searchAndAddNewsItems)
            addAll(items)
        }
        rvListNews.adapter = NewsAdapter(totalItems) {
            newsPresenter.navigateByItemType(it)
        }
    }

    override fun navigateToScreen(screenId: Int, args: Bundle) {
        navigateTo(screenId, args)
    }
}
