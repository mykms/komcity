package ru.komcity.mobile.presenter

import kotlinx.coroutines.*

/**
 * Created by Aleksei Kholoimov on 2020-02-27
 * <p>
 *
 */
interface MvpView

abstract class MvpPresenter<View> {
    abstract fun onDestroy()
}

open class BasePresenter<View> : MvpPresenter<View>() {

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
        //super.onDestroy()
    }
}