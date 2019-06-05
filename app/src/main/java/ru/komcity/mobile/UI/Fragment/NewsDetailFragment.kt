package ru.komcity.mobile.UI.Fragment

import android.os.Bundle
import android.view.View
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
        //
    }
}