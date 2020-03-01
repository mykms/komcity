package ru.komcity.mobile.repository

import ru.komcity.mobile.network.ApiMethods

/**
 * Created by Aleksey on 2020.03.01
 * <p>
 *
 */
interface AnnouncementsFilterRepository

class AnnouncementsFilterRepositoryImpl constructor(private val apiMethods: ApiMethods): AnnouncementsFilterRepository