package ru.komcity.mobile.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Aleksei Kholoimov on 10.08.2020
 * <p>
 * Ошибка http-запроса
 */
data class BaseNetError(@SerializedName("code") val code: Int = 0,
                        @SerializedName("messageShort") val title: String?,
                        @SerializedName("messageFull") val message: String?) : Serializable