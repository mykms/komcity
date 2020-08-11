package ru.komcity.mobile.common.messaging

import ru.komcity.mobile.network.ApiMethods
import ru.komcity.mobile.network.enqueue
import ru.komcity.mobile.repository.model.KomcityPushDto

/**
 * Created by Aleksei Kholoimov on 16.05.2020
 * <p>
 * Сетевая работа с токенами
 */
class PushPresenter(private val api: ApiMethods) {

    fun registerToken(token: String) {
        if (token.isEmpty()) {
            return
        }
        val request = api.registerPushToken(KomcityPushDto(token))
        request.enqueue {
            onResponse = {
                //
            }
            onFailure = {
                //
            }
        }
    }
}