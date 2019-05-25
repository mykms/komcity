package ru.komcity.mobile.UI.Adapter

import android.view.View
import kotlinx.android.synthetic.main.item_news.*
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

    inner class NewsHolder(override val containerView: View) : BaseHolder<NewsItem>(containerView) {

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