package ru.komcity.mobile.repository

import ru.komcity.mobile.network.ApiMethods
import ru.komcity.mobile.repository.mapping.AnnouncementCategoryMapper
import ru.komcity.mobile.viewModel.AnnouncementCategory

/**
 * Created by Aleksey on 2020.03.01
 * <p>
 * Источник данных для фильтров новостей
 */
interface AnnouncementsFilterRepository {
    suspend fun getAnnouncementFilters(): List<AnnouncementCategory>
}

class AnnouncementsFilterRepositoryImpl constructor(private val apiMethods: ApiMethods,
                                                    private val converter: AnnouncementCategoryMapper): AnnouncementsFilterRepository {

    override suspend fun getAnnouncementFilters(): List<AnnouncementCategory> {
        return apiMethods.getAnnouncementCategories().map { item -> converter.convert(item) }
    }
}