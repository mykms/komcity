package ru.komcity.mobile.network

import retrofit2.Call
import retrofit2.http.*
import ru.komcity.mobile.repository.model.*

interface ApiMethods {

    @GET("news")
    suspend fun getNews(@Query("page") page: Int = 1): List<NewsDto>

    @GET("news")
    suspend fun getNewsDetail(@Query("id") id: Int): NewsDto

    @GET("forum")
    suspend fun getForums(): List<ForumDto>

    @GET("forum/{name}")
    suspend fun getSubForums(@Path("name") name: String): List<SubForumDto>

    @GET("forum/{name}")
    suspend fun getForumMessages(@Path("name") name: String,
                                 @Query("id") id: Int? = null): List<ForumMessageDto>

    @GET("announcement/categories")
    suspend fun getAnnouncementCategories(): List<AnnouncementCategoryDto>

    @GET("announcement/categories")
    suspend fun getAnnouncementSubCategories(@Query("ref1") ref1: Int,
                                             @Query("ref2") ref2: Int): List<AnnouncementSubCategoryDto>

    @GET("announcement/{id}")
    suspend fun getAnnouncements(@Path("id") id: Int): List<AnnouncementDto>

    @POST("register/push")
    fun registerPushToken(@Body token: KomcityPushDto): Call<Void>
}