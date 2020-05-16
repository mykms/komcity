package ru.komcity.mobile.repository.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Aleksei Kholoimov on 16.05.2020
 * <p>
 * Для передачи токена пуш-уведомлений
 */
class KomcityPushDto(@SerializedName("pushToken") val pushToken: String)