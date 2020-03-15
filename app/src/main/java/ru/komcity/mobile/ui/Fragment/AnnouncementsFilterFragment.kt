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
import ru.komcity.mobile.viewModel.AnnouncementCategory

class AnnouncementsFilterFragment : BaseFragment(), AnnouncementsFilterView {

    private val api = ApiNetwork().api
    private val repo = AnnouncementsFilterRepositoryImpl(api, AnnouncementCategoryMapper())
    @InjectPresenter
    lateinit var presenter: AnnouncementsFilterPresenter
    @ProvidePresenter
    fun providePresenter() = AnnouncementsFilterPresenter(repo)

    override fun getArgs(args: Bundle?) {
        presenter.init()
    }

    override fun setResourceLayout(): Int {
        return R.layout.fragment_announcement_search
    }

    override fun initComponents(view: View) {
        presenter.getFilters()
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

    override fun onAnnouncementCategory(items: List<AnnouncementCategory>) {
        //
    }
}
