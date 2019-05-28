package ru.komcity.mobile.UI.Fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_forum_list.*
import ru.komcity.mobile.Model.ForumItem
import ru.komcity.mobile.Presenter.ForumPresenter
import ru.komcity.mobile.R
import ru.komcity.mobile.View.ForumView

class ForumListFragment : BaseFragment(), ForumView {

    @InjectPresenter
    lateinit var forumPresenter: ForumPresenter

    override fun getArgs(args: Bundle?) {
        //
    }

    override fun setResourceLayout(): Int {
        return R.layout.fragment_forum_list
    }

    override fun initComponents(view: View) {
        initRecyclerView()
        forumPresenter.getForums(null)
    }

    private fun initRecyclerView() {
        rvListForum.setHasFixedSize(true)
        rvListForum.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
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