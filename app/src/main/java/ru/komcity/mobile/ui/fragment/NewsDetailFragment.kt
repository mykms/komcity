package ru.komcity.mobile.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.fragment_news_detail.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.komcity.mobile.common.Constants
import ru.komcity.mobile.viewModel.NewsItem
import ru.komcity.mobile.R
import ru.komcity.mobile.network.ApiNetwork
import ru.komcity.mobile.presenter.NewsDetailPresenter
import ru.komcity.mobile.repository.NewsRepositoryImpl
import ru.komcity.mobile.view.NewsDetailView

/**
 * Created by Aleksei Kholoimov on 14.03.2020
 * <p>
 * Fragment for screen news detail info
 */
class NewsDetailFragment : BaseFragment(), NewsDetailView {

    private val api = ApiNetwork().api
    private val repo = NewsRepositoryImpl(api)
    @InjectPresenter
    lateinit var newsPresenter: NewsDetailPresenter
    @ProvidePresenter
    fun providePresenter() = NewsDetailPresenter(repo)

    var item = NewsItem("", "", "", "", emptyList(), 0, 0)

    override fun getArgs(args: Bundle?) {
        newsPresenter.init(
                args?.getInt(Constants.EXTRA_NEWS_ID, 0) ?: 0,
                args?.getString(Constants.EXTRA_NEWS_TITLE, "") ?: "")
    }

    override fun setResourceLayout(): Int {
        return R.layout.fragment_news_detail
    }

    override fun initComponents(view: View) {
        newsPresenter.iniState()
        newsPresenter.getNewsDetail()
        initToolbar()
    }

    private fun initToolbar() = with(toolbar) {
        title = ""
        setNavigationIcon(R.drawable.vector_ic_arrow_back_white)
        setNavigationOnClickListener {
            newsPresenter.navigateToBackScreen()
        }
        inflateMenu(R.menu.menu_news_share)
        changeMenuIconColor(menu, context)
        setOnMenuItemClickListener { item: MenuItem? ->
            onMenuClick(item?.itemId)
        }
    }

    private fun changeMenuIconColor(menu: Menu, context: Context) {
        menu.findItem(R.id.menu_share)?.let {
            val drawable = it.icon.apply {
                DrawableCompat.setTint(DrawableCompat.wrap(this), ContextCompat.getColor(context, R.color.white))
            }
            it.icon = drawable
        }
    }

    private fun onMenuClick(itemId: Int?): Boolean = when (itemId) {
        R.id.menu_share -> {
            newsPresenter.changeVisibilitySharePanel()
            true
        }
        else -> false
    }

    override fun onLoading(isLoading: Boolean) {
        progress.isVisible = isLoading
    }

    override fun onNewsDetailLoaded(item: NewsItem) {
        tvDate.text = item.date
        tvTitle.text = item.title
        tvDescription.text = item.text
        imageSliderView.setItems(item.imageUrls)
        //ImageLoader(item.fullNewsUrl, mainBackdrop)
    }

    override fun navigateToScreen(screenId: Int, args: Bundle) {
        navigateTo(screenId, args)
    }

    override fun navigateToBackScreen() {
        navigateToBack()
    }

    override fun setVisibilitySharePanel(isVisible: Boolean) {
        viewShare.isVisible = isVisible
    }
}