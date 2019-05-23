package ru.komcity.mobile.UI.Holder

import android.view.View
import kotlinx.android.synthetic.main.item_news.*
import ru.komcity.mobile.Model.ImageLoader.ImageCropType
import ru.komcity.mobile.Model.ImageLoader.ImageLoader
import ru.komcity.mobile.Model.NewsItem

class NewsHolder(override val containerView: View) : BaseHolder<NewsItem>(containerView) {

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