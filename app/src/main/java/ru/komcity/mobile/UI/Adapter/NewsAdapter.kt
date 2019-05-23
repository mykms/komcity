package ru.komcity.mobile.UI.Adapter

import android.view.View
import ru.komcity.mobile.Model.NewsItem
import ru.komcity.mobile.R
import ru.komcity.mobile.UI.Holder.NewsHolder

class NewsAdapter(items: List<NewsItem>) : BaseListAdapter<NewsHolder, NewsItem>(items) {

    override fun setLayoutRes(): Int = R.layout.item_news

    override fun returnViewHolder(view: View): NewsHolder = NewsHolder(view)

    override fun onItemClicked(item: NewsItem, position: Int) {
        //
    }

    override fun setClickListener(listener: ItemClickListener<NewsItem>?) {
        //
    }
}