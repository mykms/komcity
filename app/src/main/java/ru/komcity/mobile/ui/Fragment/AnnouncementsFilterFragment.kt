package ru.komcity.mobile.ui.Fragment

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_announcement_search.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.komcity.mobile.R
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
    private val categoryListSelectorTag = "categoryListSelectorTag"
    private val subCategoryListSelectorTag = "subCategoryListSelectorTag"

    override fun getArgs(args: Bundle?) {
        presenter.init()
    }

    override fun setResourceLayout(): Int {
        return R.layout.fragment_announcement_search
    }

    override fun initComponents(view: View) {
        initCategory()
        initSubCategory()
        btShow.setOnClickListener {  }
        presenter.getFilters()
    }

    private fun initCategory() = with(viewCategory) {
        title = ""
        isCloseButtonVisible = false
        setOnCloseListener {
            presenter.onCategoryCloseClick()
        }
        setOnClickListener {
            presenter.onCategoryClick()
        }
    }

    private fun initSubCategory() = with(viewSubCategory) {
        title = ""
        isCloseButtonVisible = true
        setOnCloseListener {
            presenter.onSubCategoryCloseClick()
        }
        setOnClickListener {
            presenter.onSubCategoryClick()
        }
    }

    override fun onLoading(isLoading: Boolean) {
        progress.isVisible = isLoading
    }

    override fun navigateToScreen(screenId: Int, args: Bundle) {
        navigateTo(screenId, args)
    }

    override fun navigateToBackScreen() {
        navigateToBack()
    }

    override fun setCategoryTitle(text: String, isCloseVisible: Boolean) {
        viewCategory.title = text
        viewCategory.isCloseButtonVisible = isCloseVisible
    }

    override fun setSubCategoryCategoryTitle(text: String, isCloseVisible: Boolean) {
        viewSubCategory.title = text
        viewSubCategory.isCloseButtonVisible = isCloseVisible
    }

    override fun showCategoryDialog(items: List<String>) {
        fragmentManager?.let {
            val dialogListener = object : ListSelectorDialogListener {
                override fun onDismiss() {
                }

                override fun onCloseClick() {
                    closeDialog(categoryListSelectorTag)
                }

                override fun onSelectItem(item: String, position: Int) {
                    closeDialog(categoryListSelectorTag)
                    presenter.onCategorySelected(item, position)
                }
            }
            ListSelectorDialog(items, dialogListener).apply {
                setTitle("Категория")
            }.show(it, categoryListSelectorTag)
        }
    }

    override fun showSubCategoryDialog(items: List<String>) {
        fragmentManager?.let {
            val dialogListener = object : ListSelectorDialogListener {
                override fun onDismiss() {
                }

                override fun onCloseClick() {
                    closeDialog(subCategoryListSelectorTag)
                }

                override fun onSelectItem(item: String, position: Int) {
                    closeDialog(subCategoryListSelectorTag)
                    presenter.onSubCategorySelected(item, position)
                }
            }
            ListSelectorDialog(items, dialogListener).apply {
                setTitle("Подкатегория")
            }.show(it, subCategoryListSelectorTag)
        }
    }

    private fun closeDialog(dialogTag: String) {
        (fragmentManager?.findFragmentByTag(dialogTag) as? ListSelectorDialog)?.dismiss()
    }
}
