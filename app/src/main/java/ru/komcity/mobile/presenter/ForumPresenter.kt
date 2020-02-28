package ru.komcity.mobile.presenter

import kotlinx.coroutines.*
import moxy.InjectViewState
import ru.komcity.mobile.repository.ForumRepository
import ru.komcity.mobile.view.ForumView
import ru.komcity.mobile.viewModel.ForumItem

@InjectViewState
class ForumPresenter constructor(private val forumRepository: ForumRepository): BasePresenter<ForumView>() {
    private var forumJob: Job? = null

    fun getForums() {
        viewState.onLoading(true)
        forumJob = CoroutineScope(Dispatchers.IO).launch {
            val items = forumRepository.getForums().map {
                with(it) {
                    ForumItem(forumName, description, countReplic, countTheme, linkForum)
                }
            }
            withContext(Dispatchers.Main) {
                viewState.onLoading(false)
                viewState.onForumList(items)
            }
        }
    }

    override fun onDestroy() {
        forumJob?.cancel()
        super.onDestroy()
    }
}