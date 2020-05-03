package ru.komcity.mobile.presenter

import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.MvpView

/**
 * Created by Aleksei Kholoimov on 2020-02-27
 * <p>
 *
 */
@InjectViewState
open class BasePresenter<View : MvpView> : MvpPresenter<View>() {

    private var job: Job? = null
    protected fun getExceptionHandler(onErrorExecuteJob: (throwable: Throwable) -> Unit) = CoroutineExceptionHandler { scope, throwable ->
        job = CoroutineScope(Dispatchers.Main).launch {
            withContext(this.coroutineContext) {
                onErrorExecuteJob.invoke(throwable)
            }
        }
    }

    override fun onDestroy() {
        job?.cancel()
        super.onDestroy()
    }
}