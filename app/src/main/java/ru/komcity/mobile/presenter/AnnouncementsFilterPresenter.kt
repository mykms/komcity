package ru.komcity.mobile.presenter

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import moxy.InjectViewState
import ru.komcity.mobile.repository.AnnouncementsFilterRepository
import ru.komcity.mobile.view.AnnouncementsFilterView
import ru.komcity.mobile.viewModel.AnnouncementCategory
import ru.komcity.mobile.viewModel.AnnouncementSubCategory

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
    private var selectedCategoryPosition: Int = 0
    private var selectedSubCategoryPosition: Int = 0
    private var selectedDetailCategoryPosition: Int = 0
    private var selectedDetailSubCategoryPosition: Int = 0

    fun init() {
        selectedCategoryPosition = -1
        selectedSubCategoryPosition = -1
    }

    fun getFilters() {
        filterJob = CoroutineScope(Dispatchers.Main).launch {
            viewState.onLoading(true)
            repository.getAnnouncementFilters()
                    .catch {
                        viewState.onLoading(false)
                        it.printStackTrace()
                    }
                    .map {
                        categories.add(AnnouncementCategory(it.title, it.items))
                    }
                    .onCompletion {
                        viewState.onLoading(false)
                        selectedCategoryPosition = 0
                        selectedSubCategoryPosition = 0
                        setCategoryTitle()
                        setSubCategoryTitle()
                    }
                    .collect()
        }
    }

    private fun getFilterDetails(ref1: Int, ref2: Int) {
        filterJob = CoroutineScope(Dispatchers.Main).launch {
            viewState.onLoading(true)
            repository.getAnnouncementSubCategories(ref1, ref2)
                    .catch {
                        viewState.onLoading(false)
                        it.printStackTrace()
                    }
                    .map {
                        subCategories.add(AnnouncementSubCategory(it.title, it.items))
                    }
                    .onCompletion {
                        viewState.onLoading(false)
                        selectedDetailCategoryPosition = 0
                        selectedDetailSubCategoryPosition = 0
//                        setCategoryTitle()
//                        setSubCategoryTitle()
                    }
                    .collect()
        }
    }

    private fun setCategoryTitle() {
        if (categories.size > selectedCategoryPosition && selectedCategoryPosition >= 0) {
            val title = categories[selectedCategoryPosition].title
            viewState.setCategoryTitle(title, true)
        }
    }

    private fun setSubCategoryTitle() {
        if (categories.size > selectedCategoryPosition && selectedCategoryPosition >= 0) {
            val items = categories[selectedCategoryPosition].items
            if (items.size > selectedSubCategoryPosition && selectedSubCategoryPosition >= 0) {
                viewState.setSubCategoryCategoryTitle(items[selectedSubCategoryPosition].name, true)
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
        viewState.setSubCategoryCategoryTitle("", false)
    }

    fun onCategorySelected(item: String, position: Int) {
        selectedCategoryPosition = position
        viewState.setCategoryTitle(item, true)
        viewState.setSubCategoryCategoryTitle("", false)
    }

    fun onSubCategoryClick() {
        if (categories.size > selectedCategoryPosition && selectedCategoryPosition >= 0) {
            viewState.showSubCategoryDialog(categories[selectedCategoryPosition].items.map { it.name })
        }
    }

    fun onSubCategoryCloseClick() {
        selectedSubCategoryPosition = -1
        viewState.setSubCategoryCategoryTitle("", false)
    }

    fun onSubCategorySelected(item: String, position: Int) {
        selectedSubCategoryPosition = position
        viewState.setSubCategoryCategoryTitle(item, true)
        if (categories.size > selectedCategoryPosition && selectedCategoryPosition >= 0) {
            val items = categories[selectedCategoryPosition].items
            if (items.size > selectedSubCategoryPosition && selectedSubCategoryPosition >= 0) {
                val ref = items[selectedSubCategoryPosition]
                getFilterDetails(ref.ref1, ref.ref2)
            }
        }
    }

    override fun onDestroy() {
        filterJob?.cancel()
        super.onDestroy()
    }
}