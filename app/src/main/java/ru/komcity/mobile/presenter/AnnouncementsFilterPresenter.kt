package ru.komcity.mobile.presenter

import android.os.Bundle
import androidx.core.os.bundleOf
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import moxy.InjectViewState
import retrofit2.HttpException
import ru.komcity.mobile.R
import ru.komcity.mobile.common.Constants
import ru.komcity.mobile.network.ApiNetwork
import ru.komcity.mobile.repository.AnnouncementsFilterRepository
import ru.komcity.mobile.view.AnnouncementsFilterView
import ru.komcity.mobile.viewModel.AnnouncementCategory
import ru.komcity.mobile.viewModel.AnnouncementSubCategory
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by Aleksey on 2020.03.01
 * <p>
 * Presenter for Filtering Announcements screen
 */
@InjectViewState
class AnnouncementsFilterPresenter constructor(private val repository: AnnouncementsFilterRepository)
    : BasePresenter<AnnouncementsFilterView>() {

    private var filterJob: Job? = null
    private val categories = arrayListOf<AnnouncementCategory>()
    private val subCategories = arrayListOf<AnnouncementSubCategory>()
    private var selectedCategoryPosition: Int = -1
    private var selectedSubCategoryPosition: Int = -1
    private var selectedDetailCategoryPosition: Int = -1
    private var selectedDetailSubCategoryPosition: Int = -1

    fun init() {
        selectedCategoryPosition = -1
        selectedSubCategoryPosition = -1
        selectedDetailCategoryPosition = -1
        selectedDetailSubCategoryPosition = -1
    }

    fun getFilters() {
        viewState.onLoading(true)
        categories.clear()
        filterJob = CoroutineScope(getExceptionHandler { doOnError(it) }).launch {
            withContext(Dispatchers.IO) {
                val item = repository.getAnnouncementFilters().map {
                    categories.add(AnnouncementCategory(it.title, it.items))
                }
                item.collect()
                withContext(Dispatchers.Main) {
                    setCategoryTitle()
                    setSubCategoryTitle()
                    viewState.onLoading(false)
                }
            }
        }
    }

    private fun doOnError(throwable: Throwable) {
        viewState.onLoading(false)
        when (throwable) {
            is ConnectException -> {
                //viewState.onError("Не удается соединиться с сервером")
                navigateTo(R.id.connectionErrorFragment, bundleOf())
            }
            is UnknownHostException -> {
                //viewState.onError("Проверьте связь с интернетом или адрес сервера")
                navigateTo(R.id.connectionErrorFragment, bundleOf())
            }
            is IllegalArgumentException -> {
                viewState.onError("Произошла ошибка, попробуйте позже")
            }
            is SocketTimeoutException -> {
                //viewState.onError("Проверьте связь с интернетом и попробуйте позже")
                navigateTo(R.id.connectionErrorFragment, bundleOf())
            }
            is IOException -> {
                viewState.onError("${throwable.printStackTrace()}")
            }
            is HttpException -> {
                ApiNetwork().getErrorConverter().convert(throwable.response()?.errorBody())?.let {
                    viewState.onError("${it.message}")
                }
            }
            else -> {
                viewState.onError("Произошла ошибка, попробуйте позже\n${throwable.printStackTrace()}")
            }
        }
    }

    private fun navigateTo(screenId: Int, args: Bundle) {
        viewState.navigateToScreen(screenId, args)
    }

    private fun getFilterDetails(ref1: Int, ref2: Int) {
        viewState.onLoading(true)
        subCategories.clear()
        filterJob = CoroutineScope(getExceptionHandler { doOnError(it) }).launch {
            withContext(Dispatchers.IO) {
                val item = repository.getAnnouncementSubCategories(ref1, ref2).map {
                    subCategories.add(AnnouncementSubCategory(it.title, it.items))
                }
                item.collect()
                withContext(Dispatchers.Main) {
                    setDetailCategoryTitle()
                    setDetailSubCategoryTitle()
                    viewState.onLoading(false)
                }
            }
        }
    }

    private fun setCategoryTitle() {
        if (categories.isNotEmpty()) {
            selectedCategoryPosition = 0
        }
        if (categories.size > selectedCategoryPosition && selectedCategoryPosition >= 0) {
            val title = categories[selectedCategoryPosition].title
            viewState.setCategoryVisibility(true)
            viewState.setCategoryTitle(title, true)
        }
    }

    private fun setSubCategoryTitle() {
        if (categories.size > selectedCategoryPosition && selectedCategoryPosition >= 0) {
            val items = categories[selectedCategoryPosition].items
            if (items.isNotEmpty()) {
                selectedSubCategoryPosition = 0
            }
            if (items.size > selectedSubCategoryPosition && selectedSubCategoryPosition >= 0) {
                val ref = items[selectedSubCategoryPosition]
                viewState.setSubCategoryVisibility(true)
                viewState.setSubCategoryCategoryTitle(ref.name, true)
                getFilterDetails(ref.ref1, ref.ref2)
            }
        }
    }

    private fun setDetailCategoryTitle() {
        if (subCategories.isNotEmpty()) {
            selectedDetailCategoryPosition = 0
        }
        if (subCategories.size > selectedDetailCategoryPosition && selectedDetailCategoryPosition >= 0) {
            val title = subCategories[selectedDetailCategoryPosition].title
            viewState.setDetailCategoryVisibility(true)
            viewState.setDetailCategoryTitle(title, true)
        }
    }

    private fun setDetailSubCategoryTitle() {
        if (subCategories.size > selectedDetailCategoryPosition && selectedDetailCategoryPosition >= 0) {
            val items = subCategories[selectedDetailCategoryPosition].items
            if (items.isNotEmpty()) {
                selectedDetailSubCategoryPosition = 0
            }
            if (items.size > selectedDetailSubCategoryPosition && selectedDetailSubCategoryPosition >= 0) {
                viewState.setDetailSubCategoryVisibility(true)
                viewState.setDetailSubCategoryCategoryTitle(items[selectedDetailSubCategoryPosition].name, true)
            }
        }
    }

    fun onCategoryClick() {
        viewState.showCategoryDialog(categories.map { it.title })
    }

    fun onCategoryCloseClick() {
        selectedCategoryPosition = -1
        selectedSubCategoryPosition = -1
        viewState.setCategoryTitle("", false)
        viewState.setSubCategoryVisibility(false)
        viewState.setDetailCategoryVisibility(false)
        viewState.setDetailSubCategoryVisibility(false)
        subCategories.clear()
    }

    fun onSubCategoryClick() {
        if (categories.size > selectedCategoryPosition && selectedCategoryPosition >= 0) {
            viewState.showSubCategoryDialog(categories[selectedCategoryPosition].items.map { it.name })
        }
    }

    fun onSubCategoryCloseClick() {
        selectedSubCategoryPosition = -1
        viewState.setSubCategoryCategoryTitle("", false)
        viewState.setDetailCategoryVisibility(false)
        viewState.setDetailSubCategoryVisibility(false)
        subCategories.clear()
    }

    fun onDetailCategoryClick() {
        if (subCategories.isNotEmpty()) {
            viewState.showDetailCategoryDialog(subCategories.map { it.title })
        }
    }

    fun onDetailCategoryCloseClick() {
        selectedDetailCategoryPosition = -1
        viewState.setDetailCategoryTitle("", false)
        viewState.setDetailSubCategoryVisibility(false)
    }

    fun onDetailSubCategoryClick() {
        if (subCategories.size > selectedDetailCategoryPosition && selectedDetailCategoryPosition >= 0) {
            val items = subCategories[selectedDetailCategoryPosition].items
            if (items.isNotEmpty()) {
                viewState.showDetailSubCategoryDialog(items.map { it.name })
            }
        }
    }

    fun onDetailSubCategoryCloseClick() {
        selectedDetailSubCategoryPosition = -1
        viewState.setDetailSubCategoryCategoryTitle("", false)
    }

    fun onCategorySelected(item: String, position: Int) {
        selectedCategoryPosition = position
        viewState.setCategoryVisibility(true)
        viewState.setCategoryTitle(item, true)

        selectedSubCategoryPosition = -1
        viewState.setSubCategoryVisibility(true)
        viewState.setSubCategoryCategoryTitle("", false)

        selectedDetailCategoryPosition = -1
        viewState.setDetailCategoryVisibility(false)
        viewState.setDetailCategoryTitle("", false)

        selectedDetailSubCategoryPosition = -1
        viewState.setDetailSubCategoryVisibility(false)
        viewState.setDetailSubCategoryCategoryTitle("", false)
    }

    fun onSubCategorySelected(item: String, position: Int) {
        selectedSubCategoryPosition = position
        viewState.setSubCategoryVisibility(true)
        viewState.setSubCategoryCategoryTitle(item, true)

        selectedDetailCategoryPosition = -1
        viewState.setDetailCategoryVisibility(false)
        viewState.setDetailCategoryTitle("", false)

        selectedDetailSubCategoryPosition = -1
        viewState.setDetailSubCategoryVisibility(false)
        viewState.setDetailSubCategoryCategoryTitle("", false)

        if (categories.size > selectedCategoryPosition && selectedCategoryPosition >= 0) {
            val items = categories[selectedCategoryPosition].items
            if (items.size > selectedSubCategoryPosition && selectedSubCategoryPosition >= 0) {
                val ref = items[selectedSubCategoryPosition]
                getFilterDetails(ref.ref1, ref.ref2)
            }
        }
    }

    fun onDetailCategorySelected(item: String, position: Int) {
        selectedDetailCategoryPosition = position
        viewState.setDetailCategoryVisibility(true)
        viewState.setDetailCategoryTitle(item, true)
        selectedDetailSubCategoryPosition = -1
        if (subCategories.size > selectedDetailCategoryPosition && selectedDetailCategoryPosition >= 0) {
            viewState.setDetailSubCategoryVisibility(subCategories[selectedDetailCategoryPosition].items.isNotEmpty())
            viewState.setDetailSubCategoryCategoryTitle("", false)
        }
    }

    fun onDetailSubCategorySelected(item: String, position: Int) {
        selectedDetailSubCategoryPosition = position
        viewState.setDetailSubCategoryVisibility(true)
        viewState.setDetailSubCategoryCategoryTitle(item, true)
    }

    fun navigateToAnnouncements() {
        if (subCategories.size > selectedDetailCategoryPosition && selectedDetailCategoryPosition >= 0) {
            val items = subCategories[selectedDetailCategoryPosition].items
            if (items.size > selectedDetailSubCategoryPosition && selectedDetailSubCategoryPosition >= 0) {
                val id = items[selectedDetailSubCategoryPosition].id
                viewState.onShowClick(id)
                viewState.navigateToScreen(R.id.announcementsFragment, bundleOf(Constants.EXTRA_ANNOUNCEMENTS_ID to id))
            }
        } else {
            viewState.showMessage("Требуется дополнительный выбор из списка")
        }
    }

    override fun onDestroy() {
        filterJob?.cancel()
        super.onDestroy()
    }
}