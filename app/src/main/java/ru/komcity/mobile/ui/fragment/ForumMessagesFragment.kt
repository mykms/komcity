package ru.komcity.mobile.ui.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sharetosocial.android.SocialApp
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.komcity.mobile.R
import ru.komcity.mobile.common.Constants
import ru.komcity.mobile.common.analytic.AnalyticManager
import ru.komcity.mobile.common.analytic.AnalyticManagerImpl
import ru.komcity.mobile.databinding.FragmentForumDetailMessageBinding
import ru.komcity.mobile.network.ApiNetwork
import ru.komcity.mobile.presenter.ForumPresenter
import ru.komcity.mobile.repository.ForumRepositoryImpl
import ru.komcity.mobile.ui.adapter.ForumMessageAdapter
import ru.komcity.mobile.view.ForumView
import ru.komcity.mobile.viewModel.ForumItem
import ru.komcity.mobile.viewModel.ForumMessagesItem
import ru.komcity.mobile.viewModel.SubForumItem
import ru.komcity.uicomponent.DividerWithRemoveDecorator

class ForumMessagesFragment : BaseFragment(), ForumView {
    private var _binding: FragmentForumDetailMessageBinding? = null
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
        _binding = FragmentForumDetailMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateInit(clientId: String, context: Context) {
        analytics = AnalyticManagerImpl(clientId, context)
        analytics.onScreenOpen(Constants.SCREEN_NAME_FORUM_MESSAGES)
    }

    override fun getArgs(args: Bundle?) {
        forumPresenter.initForumMessagesState(args?.getString(Constants.EXTRA_TITLE, "") ?: "",
                args?.getString(Constants.EXTRA_FORUM_NAME, "") ?: "",
                args?.getString(Constants.EXTRA_SUB_FORUM_ID, "") ?: "")
    }

    override fun setResourceLayout(): Int {
        return R.layout.fragment_forum_detail_message
    }

    override fun initComponents(view: View) {
        initToolbar()
        initRecyclerView()
        initSocial()
        forumPresenter.getForumMessages()
    }

    private fun initToolbar() = with(binding.toolbar) {
        title = ""
        setNavigationIcon(R.drawable.vector_ic_arrow_back_white)
        setNavigationOnClickListener {
            forumPresenter.navigateToBackScreen()
        }
    }

    private fun initRecyclerView() = with(binding.rvForumMessages) {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        addItemDecoration(DividerWithRemoveDecorator(context, R.drawable.recycler_divider, 0, 1))
    }

    private fun initSocial() = with(binding.viewShare) {
        isVisible = false
        setOnSocialClickListener {
            forumPresenter.onShareSocialClick(it)
        }
    }

    override fun onCopyText(text: String) {
        analytics.onCopyTextClick(Constants.SCREEN_NAME_FORUM_MESSAGES)
        context?.let {
            copyToClipBoardBuffer(it, text)
            onMessage("Скопировано")
        }
    }

    private fun copyToClipBoardBuffer(context: Context, text: String) {
        (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).apply {
            setPrimaryClip(ClipData.newPlainText("forum_copy_text", text))
        }
    }

    override fun showSocial() {
        analytics.onShareInfoClick(Constants.SCREEN_NAME_FORUM_MESSAGES)
        if (!binding.viewShare.isVisible) {
            binding.viewShare.isVisible = true
        }
    }

    override fun hideSocial() {
        activity?.runOnUiThread {
            if (binding.viewShare.isVisible) {
                binding.viewShare.isVisible = false
            }
        }
    }

    override fun onShareSocial(item: SocialApp, description: String) {
        binding.viewShare.shareMedia(item, null, description)
        analytics.onShareInfoComplete(Constants.SCREEN_NAME_FORUM_MESSAGES, item.name)
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
    }

    override fun onForumMessages(items: List<ForumMessagesItem>) {
        binding.rvForumMessages.adapter = ForumMessageAdapter(items) { text, isShare ->
            forumPresenter.onForumMessageAction(text, isShare)
        }
    }
}
