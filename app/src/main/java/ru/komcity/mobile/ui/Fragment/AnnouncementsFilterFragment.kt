package ru.komcity.mobile.ui.Fragment

import android.os.Bundle
import android.view.View
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.komcity.mobile.R
import ru.komcity.mobile.network.ApiNetwork
import ru.komcity.mobile.presenter.AnnouncementsFilterPresenter
import ru.komcity.mobile.repository.AnnouncementsFilterRepositoryImpl
import ru.komcity.mobile.view.AnnouncementsFilterView

class AnnouncementsFilterFragment : BaseFragment(), AnnouncementsFilterView {

    private val api = ApiNetwork().api
    private val repo = AnnouncementsFilterRepositoryImpl(api)
    @InjectPresenter
    lateinit var presenter: AnnouncementsFilterPresenter
    @ProvidePresenter
    fun providePresenter() = AnnouncementsFilterPresenter(repo)

    override fun getArgs(args: Bundle?) {
    }

    override fun setResourceLayout(): Int {
        return R.layout.fragment_announcement_search
    }

    override fun initComponents(view: View) {
        //
    }

    override fun onLoading(isLoading: Boolean) {
        //
    }
}
