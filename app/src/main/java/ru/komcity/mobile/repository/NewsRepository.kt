package ru.komcity.mobile.repository

import ru.komcity.mobile.common.DateTimePattern
import ru.komcity.mobile.common.DateTimeUtils
import ru.komcity.mobile.common.toCalendar
import ru.komcity.mobile.network.ApiMethods
import ru.komcity.mobile.viewModel.NewsItem
import java.util.*

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
                val humanTime = DateTimeUtils.toHumanDateTime(convertDate(date))
                NewsItem(title, humanTime, shortText, previewImg, imageUrls, newsId.toIntOrNull() ?: 0, forumId.toIntOrNull() ?: 0)
            }
        }
    }

    private fun convertDate(dateTime: String): Calendar {
        return dateTime.toCalendar(DateTimePattern.DATE_TIME_ZONE.pattern)
    }

    override suspend fun getNewsDetail(id: Int): NewsItem {
        return with(apiMethods.getNewsDetail(id)) {
            val humanTime = DateTimeUtils.toHumanDateTime(convertDate(date))
            NewsItem(title, humanTime, shortText, previewImg, imageUrls, newsId.toIntOrNull() ?: 0, forumId.toIntOrNull() ?: 0)
        }
    }
}