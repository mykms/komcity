package ru.komcity.mobile.ui.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import com.imageslider.android.ImageSliderCallback
import com.sharetosocial.android.SocialApp
import kotlinx.android.synthetic.main.fragment_news_detail.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.komcity.mobile.R
import ru.komcity.mobile.common.Constants
import ru.komcity.mobile.network.ApiNetwork
import ru.komcity.mobile.presenter.NewsDetailPresenter
import ru.komcity.mobile.repository.NewsRepositoryImpl
import ru.komcity.mobile.view.NewsDetailView
import ru.komcity.mobile.viewModel.NewsItem
import java.io.File

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
                args?.getString(Constants.EXTRA_TITLE, "") ?: "")
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

    override fun onError(message: String) {
        onMessage(message)
    }

    override fun onNewsDetailLoaded(item: NewsItem) {
        tvDate.text = item.date
        tvTitle.text = item.title
        tvDescription.text = item.text
        imageSliderView.apply {
            setOnImageClickListener(object : ImageSliderCallback {
                override fun onImageClick(bitmap: Bitmap?, position: Int) {
                }

                override fun onImageLoaded(bitmap: Bitmap?, position: Int) {
                }

                override fun onImageSwipe(bitmap: Bitmap?, position: Int) {
                }
            })
            setItems(item.imageUrls)
        }
    }

    override fun navigateToScreen(screenId: Int, args: Bundle) {
        navigateTo(screenId, args)
    }

    override fun navigateToBackScreen() {
        navigateToBack()
    }

    override fun setToolbarTitle(title: String) {
        toolbar.title = title
    }

    override fun setVisibilitySharePanel(isVisible: Boolean) {
        viewShare.isVisible = isVisible
        viewShare.setOnSocialClickListener {
            // Запросить права на сохранение медиа
            newsPresenter.onShareSocialClick(it)
        }
    }

    override fun checkStoragePermissions() {
        activity?.let {
            if (ContextCompat.checkSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                newsPresenter.onStoragePermissionsGranted()
            } else {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                        Constants.REQUEST_PERMISSION_STORAGE)
            }
        }
    }

    override fun onShareSocial(item: SocialApp, position: Int) {
        val bmp = imageSliderView.getImageByPosition(position)
        newsPresenter.saveFileOnDisk(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES), bmp)
    }

    override fun onSaveMediaSuccess(item: SocialApp, file: File) {
        val text = tvDescription.text?.toString() ?: ""
        viewShare.shareMedia(item, file, text)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_PERMISSION_STORAGE && grantResults.size == 2) {
            if (permissions[0] == Manifest.permission.WRITE_EXTERNAL_STORAGE &&
                    permissions[1] == Manifest.permission.READ_EXTERNAL_STORAGE &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                newsPresenter.onStoragePermissionsGranted()
            } else {
                newsPresenter.onStoragePermissionsDeny()
            }
        }
    }
}