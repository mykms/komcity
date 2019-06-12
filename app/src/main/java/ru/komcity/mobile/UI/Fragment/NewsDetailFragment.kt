package ru.komcity.mobile.UI.Fragment

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_news_detail.*
import ru.komcity.mobile.Common.Constants
import ru.komcity.mobile.Model.NewsItem
import ru.komcity.mobile.R

class NewsDetailFragment : BaseFragment() {

    var item = NewsItem("", "", "", "", "")

    override fun getArgs(args: Bundle?) {
        item = args?.getParcelable(Constants.EXTRA_NEWS_ITEM) ?: item
    }

    override fun setResourceLayout(): Int {
        return R.layout.fragment_news_detail
    }

    override fun initComponents(view: View) {
        tvDate.text = item.date
        tvTitle.text = item.title
        tvDescription.text = item.shortText
        imageSliderView.setItems(arrayListOf(item.fullNewsUrl, item.fullNewsUrl, item.fullNewsUrl))
        //ImageLoader(item.fullNewsUrl, mainBackdrop)
    }
}