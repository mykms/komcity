package ru.komcity.mobile.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.komcity.mobile.repository.model.ForumDto
import ru.komcity.mobile.repository.model.ForumMessageDto
import ru.komcity.mobile.repository.model.NewsDto
import ru.komcity.mobile.repository.model.SubForumDto

interface ApiMethods {

    @GET("news")
    suspend fun getNews(): List<NewsDto>

    @GET("forum")
    suspend fun getForums(): List<ForumDto>

    @GET("forum/{name}")
    suspend fun getSubForums(@Path("name") name: String): List<SubForumDto>

    @GET("forum/{name}")
    suspend fun getForumMessages(@Path("name") name: String,
                                 @Query("id") id: Int? = null): List<ForumMessageDto>

    @GET("announcement/categories")
    suspend fun getAnnouncementCategories(): Call<List<String>>

    @GET("announcement/categories")
    suspend fun getAnnouncementSubCategories(@Query("ref1") ref1: Int,
                                             @Query("ref2") ref2: Int): List<String>

    @GET("announcement/{id}")
    suspend fun getAnnouncements(@Path("id") id: Int): List<String>
}