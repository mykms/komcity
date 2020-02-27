package ru.komcity.mobile.ui.Adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import ru.komcity.mobile.viewModel.ImageLoader.ImageCropType
import ru.komcity.mobile.viewModel.ImageLoader.ImageLoader
import ru.komcity.mobile.viewModel.NewsItem
import ru.komcity.mobile.R
import ru.komcity.mobile.ui.Holder.BaseHolder

class NewsAdapter(items: List<NewsItem>) : BaseListAdapter<NewsAdapter.NewsHolder, NewsItem>(items) {
    private var itemListener: ItemClickListener<NewsItem>? = null

    override fun setLayoutRes(): Int = R.layout.item_news

    override fun returnViewHolder(view: View): NewsHolder = NewsHolder(view)

    override fun onItemClicked(item: NewsItem, position: Int) {
        this.itemListener?.onItemClick(item, position)
    }

    override fun setClickListener(listener: ItemClickListener<NewsItem>?) {
        this.itemListener = listener
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
            ImageLoader(item.previewImg, ivNews, ImageCropType.CROP_NO)
        }

        override fun setItemClickListener(listener: View.OnClickListener?) {
            tvNewsDate.setOnClickListener(listener)
            tvNewsTitle.setOnClickListener(listener)
            tvNewsShortText.setOnClickListener(listener)
            ivNews.setOnClickListener(listener)
        }
    }
}