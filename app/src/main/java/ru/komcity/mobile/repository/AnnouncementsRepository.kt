package ru.komcity.mobile.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOn
import ru.komcity.mobile.network.ApiMethods
import ru.komcity.mobile.repository.mapping.AnnouncementsMapper
import ru.komcity.mobile.viewModel.Announcement

/**
 * Created by Aleksey on 2020.03.01
 * <p>
 * Источник данных для списка объявлений
 */
interface AnnouncementsRepository {
    suspend fun getAnnouncements(id: Int): Flow<Announcement>
}

class AnnouncementsRepositoryImpl constructor(private val apiMethods: ApiMethods,
                                              private val converter: AnnouncementsMapper): AnnouncementsRepository {

    override suspend fun getAnnouncements(id: Int): Flow<Announcement> {
        return apiMethods.getAnnouncements(id)
                .map {
                    item -> converter.convert(item)
                }
                .asFlow()
                .flowOn(Dispatchers.IO)
    }
}