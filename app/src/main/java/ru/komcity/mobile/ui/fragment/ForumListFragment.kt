package ru.komcity.mobile.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_forum_list.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.komcity.mobile.R
import ru.komcity.mobile.common.Constants
import ru.komcity.mobile.network.ApiNetwork
import ru.komcity.mobile.presenter.ForumPresenter
import ru.komcity.mobile.repository.ForumRepositoryImpl
import ru.komcity.mobile.ui.adapter.ForumAdapter
import ru.komcity.mobile.view.ForumView
import ru.komcity.mobile.viewModel.ForumItem
import ru.komcity.mobile.viewModel.ForumMessagesItem
import ru.komcity.mobile.viewModel.SubForumItem

class ForumListFragment : BaseFragment(), ForumView {

    private val api = ApiNetwork().api
    private val repo = ForumRepositoryImpl(api)
    @InjectPresenter
    lateinit var forumPresenter: ForumPresenter
    @ProvidePresenter
    fun providePresenter() = ForumPresenter(repo)

    override fun getArgs(args: Bundle?) {
    }

    override fun setResourceLayout(): Int {
        return R.layout.fragment_forum_list
    }

    override fun initComponents(view: View) {
        initRecyclerView()
        forumPresenter.getForums()
    }

    private fun initRecyclerView() = with(rvListForum) {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    override fun onLoading(isLoading: Boolean) {
        progress.isVisible = isLoading
    }

    override fun onForumList(items: List<ForumItem>) {
        rvListForum.adapter = ForumAdapter(items) {
            navigateTo(R.id.forumSubListDetailFragment, bundleOf(Constants.EXTRA_FORUM_ID to it))
        }
    }

    override fun onSubForumList(items: List<SubForumItem>) {
    }

    override fun onForumMessages(items: List<ForumMessagesItem>) {
    }
}