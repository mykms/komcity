package ru.komcity.mobile.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.fragment_announcement_search.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.komcity.mobile.R
import ru.komcity.mobile.common.Constants
import ru.komcity.mobile.common.analytic.AnalyticManager
import ru.komcity.mobile.common.analytic.AnalyticManagerImpl
import ru.komcity.mobile.network.ApiNetwork
import ru.komcity.mobile.presenter.AnnouncementsFilterPresenter
import ru.komcity.mobile.repository.AnnouncementsFilterRepositoryImpl
import ru.komcity.mobile.repository.mapping.AnnouncementCategoryMapper
import ru.komcity.mobile.view.AnnouncementsFilterView
import ru.komcity.uicomponent.listselector.ListSelectorDialog
import ru.komcity.uicomponent.listselector.ListSelectorDialogListener

class AnnouncementsFilterFragment : BaseFragment(), AnnouncementsFilterView {

    private val api = ApiNetwork().api
    private val repo = AnnouncementsFilterRepositoryImpl(api, AnnouncementCategoryMapper())
    @InjectPresenter
    lateinit var presenter: AnnouncementsFilterPresenter
    @ProvidePresenter
    fun providePresenter() = AnnouncementsFilterPresenter(repo)
    private lateinit var analytics: AnalyticManager
    private val categoryListSelectorTag = "categoryListSelectorTag"
    private val subCategoryListSelectorTag = "subCategoryListSelectorTag"
    private val detailCategoryListSelectorTag = "detailCategoryListSelectorTag"
    private val detailSubCategoryListSelectorTag = "detailSubCategoryListSelectorTag"

    override fun onCreateInit(clientId: String, context: Context) {
        analytics = AnalyticManagerImpl(clientId, context)
        analytics.onScreenOpen(Constants.SCREEN_NAME_ANNOUNCEMENT_FILTER)
    }

    override fun getArgs(args: Bundle?) {
        presenter.init()
    }

    override fun setResourceLayout(): Int {
        return R.layout.fragment_announcement_search
    }

    override fun initComponents(view: View) {
        initCategory()
        initSubCategory()
        initDetailCategory()
        initDetailSubCategory()
        btShow.setOnClickListener {
            presenter.navigateToAnnouncements()
        }
        presenter.getFilters()
    }

    private fun initCategory() = with(viewCategory) {
        title = ""
        isCloseButtonVisible = false
        isVisible = false
        setOnCloseListener {
            presenter.onCategoryCloseClick()
        }
        setOnClickListener {
            presenter.onCategoryClick()
        }
    }

    private fun initSubCategory() = with(viewSubCategory) {
        title = ""
        isCloseButtonVisible = false
        isVisible = false
        setOnCloseListener {
            presenter.onSubCategoryCloseClick()
        }
        setOnClickListener {
            presenter.onSubCategoryClick()
        }
    }

    private fun initDetailCategory() = with(viewDetailCategory) {
        title = ""
        isCloseButtonVisible = false
        isVisible = false
        setOnCloseListener {
            presenter.onDetailCategoryCloseClick()
        }
        setOnClickListener {
            presenter.onDetailCategoryClick()
        }
    }

    private fun initDetailSubCategory() = with(viewDetailSubCategory) {
        title = ""
        isCloseButtonVisible = false
        isVisible = false
        setOnCloseListener {
            presenter.onDetailSubCategoryCloseClick()
        }
        setOnClickListener {
            presenter.onDetailSubCategoryClick()
        }
    }

    override fun onLoading(isLoading: Boolean) {
        progress.isVisible = isLoading
    }

    override fun navigateToScreen(screenId: Int, args: Bundle) {
        navigateTo(screenId, args)
    }

    override fun onError(message: String) {
        onMessage(message)
    }

    override fun showMessage(message: String) {
        onMessage(message)
    }

    override fun navigateToBackScreen() {
        navigateToBack()
    }

    override fun setCategoryTitle(text: String, isCloseVisible: Boolean) {
        viewCategory.title = text
        viewCategory.isCloseButtonVisible = isCloseVisible
    }

    override fun setCategoryVisibility(isVisible: Boolean) {
        viewCategory.isVisible = isVisible
    }

    override fun setSubCategoryCategoryTitle(text: String, isCloseVisible: Boolean) {
        viewSubCategory.title = text
        viewSubCategory.isCloseButtonVisible = isCloseVisible
    }

    override fun setSubCategoryVisibility(isVisible: Boolean) {
        viewSubCategory.isVisible = isVisible
    }

    override fun setDetailCategoryTitle(text: String, isCloseVisible: Boolean) {
        viewDetailCategory.title = text
        viewDetailCategory.isCloseButtonVisible = isCloseVisible
    }

    override fun setDetailCategoryVisibility(isVisible: Boolean) {
        viewDetailCategory.isVisible = isVisible
    }

    override fun setDetailSubCategoryCategoryTitle(text: String, isCloseVisible: Boolean) {
        viewDetailSubCategory.title = text
        viewDetailSubCategory.isCloseButtonVisible = isCloseVisible
    }

    override fun setDetailSubCategoryVisibility(isVisible: Boolean) {
        viewDetailSubCategory.isVisible = isVisible
    }

    override fun showCategoryDialog(items: List<String>) {
        fragmentManager?.let {
            val dialogListener = object : ListSelectorDialogListener {

                override fun onCloseClick() {
                    closeDialog(categoryListSelectorTag)
                }

                override fun onSelectItem(item: String, position: Int) {
                    closeDialog(categoryListSelectorTag)
                    presenter.onCategorySelected(item, position)
                }
            }
            ListSelectorDialog().apply {
                setParams(items, dialogListener)
                setTitle("Категория")
            }.show(it, categoryListSelectorTag)
        }
    }

    override fun showSubCategoryDialog(items: List<String>) {
        fragmentManager?.let {
            val dialogListener = object : ListSelectorDialogListener {

                override fun onCloseClick() {
                    closeDialog(subCategoryListSelectorTag)
                }

                override fun onSelectItem(item: String, position: Int) {
                    closeDialog(subCategoryListSelectorTag)
                    presenter.onSubCategorySelected(item, position)
                }
            }
            ListSelectorDialog().apply {
                setParams(items, dialogListener)
                setTitle("Подкатегория")
            }.show(it, subCategoryListSelectorTag)
        }
    }

    override fun showDetailCategoryDialog(items: List<String>) {
        fragmentManager?.let {
            val dialogListener = object : ListSelectorDialogListener {

                override fun onCloseClick() {
                    closeDialog(detailCategoryListSelectorTag)
                }

                override fun onSelectItem(item: String, position: Int) {
                    closeDialog(detailCategoryListSelectorTag)
                    presenter.onDetailCategorySelected(item, position)
                }
            }
            ListSelectorDialog().apply {
                setParams(items, dialogListener)
                setTitle("Детали подкатегории")
            }.show(it, detailCategoryListSelectorTag)
        }
    }

    override fun showDetailSubCategoryDialog(items: List<String>) {
        fragmentManager?.let {
            val dialogListener = object : ListSelectorDialogListener {

                override fun onCloseClick() {
                    closeDialog(detailSubCategoryListSelectorTag)
                }

                override fun onSelectItem(item: String, position: Int) {
                    closeDialog( detailSubCategoryListSelectorTag)
                    presenter.onDetailSubCategorySelected(item, position)
                }
            }
            ListSelectorDialog().apply {
                setParams(items, dialogListener)
                setTitle("Выберите значение")
            }.show(it, detailSubCategoryListSelectorTag)
        }
    }

    override fun onShowClick(listId: String) {
        analytics.onAnnouncementShowClick(listId)
    }

    private fun closeDialog(dialogTag: String) {
        (fragmentManager?.findFragmentByTag(dialogTag) as? ListSelectorDialog)?.dismiss()
    }
}
