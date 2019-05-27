package ru.komcity.mobile.UI.Adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import ru.komcity.mobile.Model.ImageLoader.ImageCropType
import ru.komcity.mobile.Model.ImageLoader.ImageLoader
import ru.komcity.mobile.Model.NewsItem
import ru.komcity.mobile.R
import ru.komcity.mobile.UI.Holder.BaseHolder

class NewsAdapter(items: List<NewsItem>) : BaseListAdapter<NewsAdapter.NewsHolder, NewsItem>(items) {

    override fun setLayoutRes(): Int = R.layout.item_news

    override fun returnViewHolder(view: View): NewsHolder = NewsHolder(view)

    override fun onItemClicked(item: NewsItem, position: Int) {
        //
    }

    override fun setClickListener(listener: ItemClickListener<NewsItem>?) {
        //
    }

    inner class NewsHolder(containerView: View) : BaseHolder<NewsItem>(containerView) {
        private val tvNewsDate = containerView.findViewById<TextView>(R.id.tvNewsDate)
        private val tvNewsTitle = containerView.findViewById<TextView>(R.id.tvNewsTitle)
        private val tvNewsShortText = containerView.findViewById<TextView>(R.id.tvNewsShortText)
        private val ivNews = containerView.findViewById<ImageView>(R.id.ivNews)

        override fun setData(item: NewsItem) {
            tvNewsDate.text = item.date
            tvNewsTitle.text = item.title
            tvNewsShortText.text = item.shortText
            ImageLoader(item.imageUrl, ivNews, ImageCropType.CROP_ROUNDED)
        }

        override fun setItemClickListener(listener: View.OnClickListener?) {
            //
        }
    }
}