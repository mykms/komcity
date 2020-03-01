package ru.komcity.mobile.ui.Fragment

import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_announcements.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.komcity.mobile.R
import ru.komcity.mobile.network.ApiNetwork
import ru.komcity.mobile.presenter.AnnouncementsPresenter
import ru.komcity.mobile.repository.AnnouncementsRepositoryImpl
import ru.komcity.mobile.view.AnnouncementsView

class AnnouncementsFragment : BaseFragment(), AnnouncementsView {

    private val api = ApiNetwork().api
    private val repo = AnnouncementsRepositoryImpl(api)
    @InjectPresenter
    lateinit var presenter: AnnouncementsPresenter
    @ProvidePresenter
    fun providePresenter() = AnnouncementsPresenter(repo)

    override fun getArgs(args: Bundle?) {
        //
    }

    override fun setResourceLayout(): Int {
        return R.layout.fragment_announcements
    }

    override fun initComponents(view: View) {
        initRecyclerView(view)
        presenter.getAnnouncements()
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
}