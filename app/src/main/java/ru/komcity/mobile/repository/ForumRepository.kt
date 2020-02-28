package ru.komcity.mobile.repository

import ru.komcity.mobile.network.ApiMethods
import ru.komcity.mobile.repository.model.ForumDto
import ru.komcity.mobile.repository.model.ForumMessageDto
import ru.komcity.mobile.repository.model.SubForumDto

interface ForumRepository {
    suspend fun getForums(): List<ForumDto>
    suspend fun getSubForums(forumName: String): List<SubForumDto>
    suspend fun getForumMessages(forumName: String, id: Int): List<ForumMessageDto>
}

class ForumRepositoryImpl constructor(private val apiMethods: ApiMethods): ForumRepository {

    override suspend fun getForums(): List<ForumDto> {
        return apiMethods.getForums()
    }

    override suspend fun getSubForums(forumName: String): List<SubForumDto> {
        return apiMethods.getSubForums(forumName)
    }

    override suspend fun getForumMessages(forumName: String, id: Int): List<ForumMessageDto> {
        return apiMethods.getForumMessages(forumName, id)
    }
}