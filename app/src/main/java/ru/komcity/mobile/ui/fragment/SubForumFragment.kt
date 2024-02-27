package ru.komcity.mobile.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sharetosocial.android.SocialApp
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.komcity.mobile.R
import ru.komcity.mobile.common.Constants
import ru.komcity.mobile.common.analytic.AnalyticManager
import ru.komcity.mobile.common.analytic.AnalyticManagerImpl
import ru.komcity.mobile.databinding.FragmentForumSubListDetailBinding
import ru.komcity.mobile.network.ApiNetwork
import ru.komcity.mobile.presenter.ForumPresenter
import ru.komcity.mobile.repository.ForumRepositoryImpl
import ru.komcity.mobile.ui.adapter.SubForumAdapter
import ru.komcity.mobile.view.ForumView
import ru.komcity.mobile.viewModel.ForumItem
import ru.komcity.mobile.viewModel.ForumMessagesItem
import ru.komcity.mobile.viewModel.SubForumItem
import ru.komcity.uicomponent.DividerWithRemoveDecorator

class SubForumFragment : BaseFragment(), ForumView {
    private var _binding: FragmentForumSubListDetailBinding? = null
    private val binding get() = _binding!!
    private val api = ApiNetwork().api
    private val repo = ForumRepositoryImpl(api)
    @InjectPresenter
    lateinit var forumPresenter: ForumPresenter
    @ProvidePresenter
    fun providePresenter() = ForumPresenter(repo)
    private lateinit var analytics: AnalyticManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForumSubListDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateInit(clientId: String, context: Context) {
        analytics = AnalyticManagerImpl(clientId, context)
        analytics.onScreenOpen(Constants.SCREEN_NAME_FORUM_DETAILS)
    }

    override fun getArgs(args: Bundle?) {
        forumPresenter.initSubForumState(args?.getString(Constants.EXTRA_TITLE, "") ?: "",
                args?.getString(Constants.EXTRA_FORUM_NAME, "") ?: "")
    }

    override fun setResourceLayout(): Int {
        return R.layout.fragment_forum_sub_list_detail
    }

    override fun initComponents(view: View) {
        initToolbar()
        initRecyclerView()
        forumPresenter.getSubForum()
    }

    private fun initToolbar() = with(binding.toolbar) {
        title = ""
        setNavigationIcon(R.drawable.vector_ic_arrow_back_white)
        setNavigationOnClickListener {
            forumPresenter.navigateToBackScreen()
        }
    }

    private fun initRecyclerView() = with(binding.rvSubForum) {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        addItemDecoration(DividerWithRemoveDecorator(context, R.drawable.recycler_divider, 0, 1))
    }

    override fun onLoading(isLoading: Boolean) {
        binding.progress.isVisible = isLoading
    }

    override fun onError(message: String) {
        onMessage(message)
    }

    override fun navigateToScreen(screenId: Int, args: Bundle) {
        navigateTo(screenId, args)
    }

    override fun navigateToBackScreen() {
        navigateToBack()
    }

    override fun setToolbarTitle(title: String) {
        binding.toolbar.title = title
    }

    override fun onForumList(items: List<ForumItem>) {
    }

    override fun onSubForumList(items: List<SubForumItem>, forumName: String) {
        binding.rvSubForum.adapter = SubForumAdapter(items) { title, forumId ->
            analytics.onForumMessagesClick(forumId)
            navigateTo(R.id.forumDetailMessageFragment, bundleOf(
                    Constants.EXTRA_TITLE to title,
                    Constants.EXTRA_FORUM_NAME to forumName,
                    Constants.EXTRA_SUB_FORUM_ID to forumId))
        }
    }

    override fun onForumMessages(items: List<ForumMessagesItem>) {
    }

    override fun onCopyText(text: String) {
    }

    override fun showSocial() {
    }

    override fun hideSocial() {
    }

    override fun onShareSocial(item: SocialApp, description: String) {
    }
}
