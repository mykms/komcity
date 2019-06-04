package ru.komcity.mobile.UI.Fragment

import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_news_list.*
import ru.komcity.mobile.Model.NewsItem
import ru.komcity.mobile.Presenter.NewsPresenter
import ru.komcity.mobile.R
import ru.komcity.mobile.UI.Adapter.ItemClickListener
import ru.komcity.mobile.UI.Adapter.NewsAdapter
import ru.komcity.mobile.View.NewsListView

class NewsListFragment : BaseFragment(), NewsListView, ItemClickListener<NewsItem> {

    @InjectPresenter
    lateinit var newsPresenter: NewsPresenter

    override fun getArgs(args: Bundle?) {
        //
    }

    override fun setResourceLayout(): Int {
        return R.layout.fragment_news_list
    }

    override fun initComponents(view: View) {
        initRecyclerView(view)
        newsPresenter.getNews()
    }

    private fun initRecyclerView(view: View) {
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(view.context, R.drawable.recycler_divider) ?: ShapeDrawable())
        rvListNews.setHasFixedSize(true)
        rvListNews.layoutManager = layoutManager
        rvListNews.addItemDecoration(dividerItemDecoration)
    }

    override fun onNewsLoaded(items: List<NewsItem>) {
        val adapter = NewsAdapter(items)
        adapter.setClickListener(this)
        rvListNews.adapter = adapter
//        val skeletonScreen = Skeleton.bind(rvListNews)
//                .adapter(NewsAdapter(items))
//                .load(R.layout.item_preloading_list)
//                .shimmer(true)      // whether show shimmer animation.                      default is true
//                .count(10)          // the recycler view item count.                        default is 10
//                .color(R.color.colorPrimaryDark)       // the shimmer color.                                   default is #a2878787
//                .angle(20)          // the shimmer angle.                                   default is 20;
//                .duration(1000)     // the shimmer animation duration.                      default is 1000;
//                .frozen(false)      // whether frozen recyclerView during skeleton showing  default is true;
//                .show()
    }

    override fun onItemClick(item: NewsItem, position: Int) {
        //newsPresenter.getNews()
    }
}
