package ru.komcity.mobile.UI.Fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_news_list.*
import ru.komcity.mobile.R
import ru.komcity.mobile.UI.Adapter.NewsAdapter

class NewsListFragment : BaseFragment() {

    override fun getArgs(args: Bundle?) {
        //
    }

    override fun setResourceLayout(): Int {
        return R.layout.fragment_news_list
    }

    override fun initComponents(view: View) {
        initRecyclerView()
        rvListNews.adapter = NewsAdapter(listOf())
    }

    private fun initRecyclerView() {
        rvListNews.setHasFixedSize(true)
        rvListNews.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }
}
