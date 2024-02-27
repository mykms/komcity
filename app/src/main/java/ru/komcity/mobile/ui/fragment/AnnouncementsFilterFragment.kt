package ru.komcity.mobile.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.komcity.mobile.R
import ru.komcity.mobile.common.Constants
import ru.komcity.mobile.common.analytic.AnalyticManager
import ru.komcity.mobile.common.analytic.AnalyticManagerImpl
import ru.komcity.mobile.databinding.FragmentAnnouncementSearchBinding
import ru.komcity.mobile.network.ApiNetwork
import ru.komcity.mobile.presenter.AnnouncementsFilterPresenter
import ru.komcity.mobile.repository.AnnouncementsFilterRepositoryImpl
import ru.komcity.mobile.repository.mapping.AnnouncementCategoryMapper
import ru.komcity.mobile.view.AnnouncementsFilterView
import ru.komcity.uicomponent.listselector.ListSelectorDialog
import ru.komcity.uicomponent.listselector.ListSelectorDialogListener

class AnnouncementsFilterFragment : BaseFragment(), AnnouncementsFilterView {
    private var _binding: FragmentAnnouncementSearchBinding? = null
    private val binding get() = _binding!!
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnnouncementSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initComponents(view: View) {
        initCategory()
        initSubCategory()
        initDetailCategory()
        initDetailSubCategory()
        binding.btShow.setOnClickListener {
            presenter.navigateToAnnouncements()
        }
        presenter.getFilters()
    }

    private fun initCategory() = with(binding.viewCategory) {
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

    private fun initSubCategory() = with(binding.viewSubCategory) {
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

    private fun initDetailCategory() = with(binding.viewDetailCategory) {
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

    private fun initDetailSubCategory() = with(binding.viewDetailSubCategory) {
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
        binding.progress.isVisible = isLoading
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
        binding.viewCategory.title = text
        binding.viewCategory.isCloseButtonVisible = isCloseVisible
    }

    override fun setCategoryVisibility(isVisible: Boolean) {
        binding.viewCategory.isVisible = isVisible
    }

    override fun setSubCategoryCategoryTitle(text: String, isCloseVisible: Boolean) {
        binding.viewSubCategory.title = text
        binding.viewSubCategory.isCloseButtonVisible = isCloseVisible
    }

    override fun setSubCategoryVisibility(isVisible: Boolean) {
        binding.viewSubCategory.isVisible = isVisible
    }

    override fun setDetailCategoryTitle(text: String, isCloseVisible: Boolean) {
        binding.viewDetailCategory.title = text
        binding.viewDetailCategory.isCloseButtonVisible = isCloseVisible
    }

    override fun setDetailCategoryVisibility(isVisible: Boolean) {
        binding.viewDetailCategory.isVisible = isVisible
    }

    override fun setDetailSubCategoryCategoryTitle(text: String, isCloseVisible: Boolean) {
        binding.viewDetailSubCategory.title = text
        binding.viewDetailSubCategory.isCloseButtonVisible = isCloseVisible
    }

    override fun setDetailSubCategoryVisibility(isVisible: Boolean) {
        binding.viewDetailSubCategory.isVisible = isVisible
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
