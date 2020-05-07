package ru.komcity.mobile.repository

import ru.komcity.mobile.network.ApiMethods

/**
 * Created by Aleksei Kholoimov on 07.05.2020
 * <p>
 * Репозиторий отправки новости
 */
interface SendRepository {
    suspend fun getSendParams(): String
    suspend fun sendNews(subject: String, description: String)
}

class SendRepositoryImpl constructor(private val apiMethods: ApiMethods): SendRepository {
    override suspend fun getSendParams(): String {
        //apiMethods.getNews(0)
        return ""
    }

    override suspend fun sendNews(subject: String, description: String) {
        //
    }
}