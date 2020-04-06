package ru.komcity.mobile.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOn
import ru.komcity.mobile.network.ApiMethods
import ru.komcity.mobile.repository.mapping.AnnouncementCategoryMapper
import ru.komcity.mobile.viewModel.AnnouncementCategory
import ru.komcity.mobile.viewModel.AnnouncementSubCategory

/**
 * Created by Aleksey on 2020.03.01
 * <p>
 * Источник данных для фильтров новостей
 */
interface AnnouncementsFilterRepository {
    suspend fun getAnnouncementFilters(): Flow<AnnouncementCategory>
    suspend fun getAnnouncementSubCategories(ref1: Int, ref2: Int): Flow<AnnouncementSubCategory>
}

class AnnouncementsFilterRepositoryImpl constructor(private val apiMethods: ApiMethods,
                                                    private val converter: AnnouncementCategoryMapper): AnnouncementsFilterRepository {

    override suspend fun getAnnouncementFilters(): Flow<AnnouncementCategory> {
        return apiMethods.getAnnouncementCategories()
                .map { item -> converter.convert(item) }
                .asFlow()
    }

    override suspend fun getAnnouncementSubCategories(ref1: Int, ref2: Int): Flow<AnnouncementSubCategory> {
        return apiMethods.getAnnouncementSubCategories(ref1, ref2)
                .map { item -> converter.convert(item) }
                .asFlow()
                .flowOn(Dispatchers.IO)
    }
}