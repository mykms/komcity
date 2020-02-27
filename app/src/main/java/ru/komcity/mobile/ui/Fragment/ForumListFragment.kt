package ru.komcity.mobile.ui.Fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_forum_list.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.komcity.mobile.viewModel.ForumItem
import ru.komcity.mobile.presenter.ForumPresenter
import ru.komcity.mobile.R
import ru.komcity.mobile.view.ForumView

class ForumListFragment : BaseFragment(), ForumView {

    @InjectPresenter
    lateinit var forumPresenter: ForumPresenter

    @ProvidePresenter
    fun providePresenter(): ForumPresenter = ForumPresenter()

    override fun getArgs(args: Bundle?) {
        //
    }

    override fun setResourceLayout(): Int {
        return R.layout.fragment_forum_list
    }

    override fun initComponents(view: View) {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        rvListForum.setHasFixedSize(true)
        rvListForum.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    override fun onLoadingStart() {
        //
    }

    override fun onLoadingStop() {
        //
    }

    override fun onForumList(items: List<ForumItem>) {
        //
    }

    override fun onSubForumList(items: List<ForumItem>) {
        //
    }

    override fun onForumDetail(item: ForumItem) {
        //
    }
}