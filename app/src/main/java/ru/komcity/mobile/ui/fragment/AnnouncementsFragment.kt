package ru.komcity.mobile.ui.fragment

import android.content.Context
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_announcements.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.komcity.mobile.R
import ru.komcity.mobile.common.Constants
import ru.komcity.mobile.common.analytic.AnalyticManager
import ru.komcity.mobile.common.analytic.AnalyticManagerImpl
import ru.komcity.mobile.network.ApiNetwork
import ru.komcity.mobile.presenter.AnnouncementsPresenter
import ru.komcity.mobile.repository.AnnouncementsRepositoryImpl
import ru.komcity.mobile.repository.mapping.AnnouncementsMapper
import ru.komcity.mobile.ui.adapter.AnnouncementAdapter
import ru.komcity.mobile.view.AnnouncementsView
import ru.komcity.mobile.viewModel.Announcement

class AnnouncementsFragment : BaseFragment(), AnnouncementsView {

    private val api = ApiNetwork().api
    private val repo = AnnouncementsRepositoryImpl(api, AnnouncementsMapper())
    @InjectPresenter
    lateinit var presenter: AnnouncementsPresenter
    @ProvidePresenter
    fun providePresenter() = AnnouncementsPresenter(repo)
    private lateinit var analytics: AnalyticManager

    override fun onCreateInit(clientId: String, context: Context) {
        analytics = AnalyticManagerImpl(clientId, context)
        analytics.onScreenOpen(Constants.SCREEN_NAME_ANNOUNCEMENTS)
    }

    override fun getArgs(args: Bundle?) {
        args?.let {
            presenter.initState(it.getString(Constants.EXTRA_ANNOUNCEMENTS_ID, ""))
        }
    }

    override fun setResourceLayout(): Int = R.layout.fragment_announcements

    override fun initComponents(view: View) {
        initRecyclerView(view)
        initToolbar()
        presenter.getAnnouncements()
    }

    private fun initToolbar() = with(toolbar) {
        title = "Список объявлений"
        setNavigationIcon(R.drawable.vector_ic_arrow_back_white)
        setNavigationOnClickListener {
            presenter.navigateToBackScreen()
        }
    }

    private fun initRecyclerView(view: View) = with(rvAnnouncements) {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
            setDrawable(ContextCompat.getDrawable(view.context, R.drawable.recycler_divider)
                    ?: ShapeDrawable())
        })
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

    override fun navigateToBackScreen() {
        navigateToBack()
    }

    override fun showAnnouncements(items: List<Announcement>) {
        rvAnnouncements.adapter = AnnouncementAdapter(items) {
            analytics.onAnnouncementDetailsClick(it.id)
            onMessage("Нет возможности просмотреть детальную информацию")
        }
    }
}