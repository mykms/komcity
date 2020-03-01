package ru.komcity.mobile.repository

import ru.komcity.mobile.network.ApiMethods

/**
 * Created by Aleksey on 2020.03.01
 * <p>
 * Источник данных для списка объявлений
 */
interface AnnouncementsRepository

class AnnouncementsRepositoryImpl constructor(private val apiMethods: ApiMethods): AnnouncementsRepository