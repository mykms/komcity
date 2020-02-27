package ru.komcity.mobile.presenter

import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.MvpView
import retrofit2.Call
import ru.komcity.mobile.network.ApiResponse

/**
 * Created by Aleksei Kholoimov on 2020-02-27
 * <p>
 *
 */
@InjectViewState
open class BasePresenter<View : MvpView> : MvpPresenter<View>() {

//    protected fun getNetError(errorBody: ResponseBody?): BaseNetError? {
//        val converter = ApiNetwork().getErrorConverter()
//        return errorBody?.let {
//            try {
//                converter.convert(it)
//            } catch (ex: Exception) {
//                null
//            }
//        }
//    }

    protected fun <T> Call<T>.enqueue(callback: ApiResponse<T>.() -> Unit) {
        val respCallback = ApiResponse<T>()
        callback.invoke(respCallback)
        this.enqueue(respCallback)
    }
}