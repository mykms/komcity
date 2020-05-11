package ru.komcity.mobile.repository.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Aleksey on 2020.02.28
 * <p>
 * Сетевые объекты для получения форумов и сообщений
 */
class ForumDto(@SerializedName("forumName") val forumName: String,
               @SerializedName("description") val description: String,
               @SerializedName("countReplic") val countReplic: String,
               @SerializedName("countTheme") val countTheme: String,
               @SerializedName("linkForum") val linkForum: String)

class SubForumDto(@SerializedName("forumName") val forumName: String,
                  @SerializedName("description") val dateTime: String,
                  @SerializedName("countReplic") val countReplic: String,
                  @SerializedName("countTheme") val countTheme: String,
                  @SerializedName("linkForum") val linkForum: String)

class ForumMessageDto(@SerializedName("date") val date: String,
                      @SerializedName("userName") val userName: String,
                      @SerializedName("message") val message: String)