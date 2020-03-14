package ru.komcity.mobile.repository

import ru.komcity.mobile.network.ApiMethods
import ru.komcity.mobile.repository.model.NewsDto

/**
 * Created by Aleksei Kholoimov on 2020-02-27
 * <p>
 * Источник данных новостей
 */
interface NewsRepository {

    suspend fun getNews(): List<NewsDto>
    suspend fun getNewsDetail(newsId: Int): NewsDto
}

class NewsRepositoryImpl constructor(private val apiMethods: ApiMethods): NewsRepository {

    override suspend fun getNews(): List<NewsDto> {
        return apiMethods.getNews()
    }

    override suspend fun getNewsDetail(newsId: Int): NewsDto {
        //todo add server method
        return NewsDto("", "", "", "", emptyList(), "")
    }
}