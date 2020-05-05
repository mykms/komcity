package ru.komcity.mobile.presenter

import android.graphics.Bitmap
import android.os.Bundle
import com.sharetosocial.android.SocialApp
import kotlinx.coroutines.*
import moxy.InjectViewState
import ru.komcity.mobile.repository.NewsRepository
import ru.komcity.mobile.view.NewsDetailView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.UnknownHostException
import java.util.*

/**
 * Created by Aleksei Kholoimov on 14.03.2020
 * <p>
 * Presenter for screen news detail info
 */
@InjectViewState
class NewsDetailPresenter constructor(private val newsRepository: NewsRepository) : BasePresenter<NewsDetailView>() {

    private var newsJob: Job? = null
    private var newsId = 0
    private var isVisibleSharePanel = false
    private var title: String = ""
    private var swipePosition: Int = 0
    private var socialItem: SocialApp? = null

    fun init(newsId: Int, title: String) {
        this.newsId = newsId
        this.title = title
    }

    fun iniState() {
        viewState.setToolbarTitle(title)
        viewState.setVisibilitySharePanel(isVisibleSharePanel)
    }

    fun getNewsDetail() {
        viewState.onLoading(true)
        newsJob = CoroutineScope(getExceptionHandler { doOnError(it) }).launch {
            withContext(Dispatchers.IO) {
                val item = newsRepository.getNewsDetail(newsId)
                withContext(Dispatchers.Main) {
                    viewState.onNewsDetailLoaded(item)
                    viewState.onLoading(false)
                }
            }
        }
    }

    private fun doOnError(throwable: Throwable) {
        viewState.onLoading(false)
        when (throwable) {
            is UnknownHostException -> {
                viewState.onError("Проверьте связь с интернетом или адрес сервера")
            }
            is IllegalArgumentException -> {
                viewState.onError("Произошла ошибка, попробуйте позже")
            }
            else -> {}
        }
    }

    fun navigateTo(screenId: Int, args: Bundle) {
        viewState.navigateToScreen(screenId, args)
    }

    fun navigateToBackScreen() {
        viewState.navigateToBackScreen()
    }

    fun changeVisibilitySharePanel() {
        isVisibleSharePanel = !isVisibleSharePanel
        viewState.setVisibilitySharePanel(isVisibleSharePanel)
    }

    fun onSwipeImage(position: Int) {
        swipePosition = position
    }

    fun onShareSocialClick(item: SocialApp) {
        socialItem = item
        viewState.checkStoragePermissions()
    }

    fun onStoragePermissionsGranted() {
        socialItem?.let { viewState.onShareSocial(it, swipePosition) }
    }

    fun onStoragePermissionsDeny() {
        socialItem = null
        viewState.onError("Предоставьте доступ к диску вашего устройства")
    }

    fun saveFileOnDisk(directory: File?, bitmap: Bitmap?) {
        val file = File(directory, "komcity_${Calendar.getInstance().timeInMillis}.jpg")
        checkExistFile(file)
        val isSuccess = writeImageOnDisk(file, bitmap)
        if (isSuccess) {
            socialItem?.let { viewState.onSaveMediaSuccess(it, file) }
        } else {
            deleteFile(file)
        }
    }

    private fun checkExistFile(file: File) {
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun writeImageOnDisk(file: File, bitmap: Bitmap?): Boolean {
        var isSuccess = false
        try {
            var stream: FileOutputStream? = null
            try {
                stream = FileOutputStream(file.absoluteFile)
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                stream?.let {
                    it.flush()
                    it.close()
                    isSuccess = true
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            isSuccess = false
        }
        return isSuccess
    }

    private fun deleteFile(file: File) {
        try {
            file.delete()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onDestroy() {
        newsJob?.cancel()
        super.onDestroy()
    }
}