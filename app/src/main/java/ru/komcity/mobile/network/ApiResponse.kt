package ru.komcity.mobile.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Aleksei Kholoimov on 2019-10-07
 * <p>
 * Для обработки ответа сети
 */
class ApiResponse<T>: Callback<T> {

    internal var onResponse: ((Response<T>) -> Unit)? = null
    internal var onFailure: ((t: Throwable?) -> Unit)? = null

    override fun onFailure(call: Call<T>, t: Throwable) {
        onFailure?.invoke(t)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        onResponse?.invoke(response)
    }
}