package ru.komcity.mobile.UI.Fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_news_list.*
import ru.komcity.mobile.Model.NewsItem
import ru.komcity.mobile.Presenter.NewsPresenter
import ru.komcity.mobile.R
import ru.komcity.mobile.UI.Adapter.NewsAdapter
import ru.komcity.mobile.View.NewsListView

class NewsListFragment : BaseFragment(), NewsListView {

    @InjectPresenter
    lateinit var newsPresenter: NewsPresenter

    override fun getArgs(args: Bundle?) {
        //
    }

    override fun setResourceLayout(): Int {
        return R.layout.fragment_news_list
    }

    override fun initComponents(view: View) {
        initRecyclerView()
        newsPresenter.getNews()
    }

    private fun initRecyclerView() {
        rvListNews.setHasFixedSize(true)
        rvListNews.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    override fun onNewsLoaded(items: List<NewsItem>) {
        rvListNews.adapter = NewsAdapter(listOf())
    }
}
