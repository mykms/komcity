package ru.komcity.mobile.ui.fragment

import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_news_list.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.komcity.mobile.R
import ru.komcity.mobile.common.Constants
import ru.komcity.mobile.network.ApiNetwork
import ru.komcity.mobile.presenter.NewsPresenter
import ru.komcity.mobile.repository.NewsRepositoryImpl
import ru.komcity.mobile.ui.adapter.NewsAdapter
import ru.komcity.mobile.view.NewsListView
import ru.komcity.mobile.viewModel.NewsItem

class NewsListFragment: BaseFragment(), NewsListView {

    private val api = ApiNetwork().api
    private val repo = NewsRepositoryImpl(api)
    @InjectPresenter
    lateinit var newsPresenter: NewsPresenter
    @ProvidePresenter
    fun providePresenter() = NewsPresenter(repo)

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
        addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
            setDrawable(ContextCompat.getDrawable(view.context, R.drawable.recycler_divider)
                    ?: ShapeDrawable())
        })
    }

    override fun onLoading(isLoading: Boolean) {
        progress.isVisible = isLoading
    }

    override fun onNewsLoaded(items: List<NewsItem>) {
        rvListNews.adapter = NewsAdapter(items) {
            newsPresenter.navigateTo(R.id.newsDetailFragment, bundleOf(Constants.EXTRA_NEWS_ITEM to it))
        }
    }

    override fun navigateToScreen(screenId: Int, args: Bundle) {
        navigateTo(screenId, args)
    }
}
