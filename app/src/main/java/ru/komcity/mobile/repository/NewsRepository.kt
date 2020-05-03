package ru.komcity.mobile.repository

import ru.komcity.mobile.network.ApiMethods
import ru.komcity.mobile.viewModel.NewsItem

/**
 * Created by Aleksei Kholoimov on 2020-02-27
 * <p>
 * Источник данных новостей
 */
interface NewsRepository {

    suspend fun getNews(): List<NewsItem>
    suspend fun getNewsDetail(id: Int): NewsItem
}

class NewsRepositoryImpl constructor(private val apiMethods: ApiMethods): NewsRepository {

    override suspend fun getNews(): List<NewsItem> {
        return apiMethods.getNews().map {
            with(it) {
                NewsItem(title, date, shortText, previewImg, imageUrls, newsId.toIntOrNull() ?: 0, forumId.toIntOrNull() ?: 0)
            }
        }
    }

    override suspend fun getNewsDetail(id: Int): NewsItem {
        return with(apiMethods.getNewsDetail(id)) {
            NewsItem(title, date, shortText, previewImg, imageUrls, newsId.toIntOrNull() ?: 0, forumId.toIntOrNull() ?: 0)
        }
    }
}