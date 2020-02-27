package ru.komcity.mobile.repository.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Aleksei Kholoimov on 2020-02-27
 * <p>
 * Network Object - news
 */
class NewsDto(@SerializedName("title") val title: String,
              @SerializedName("date") val date: String,
              @SerializedName("shortText") val shortText: String,
              @SerializedName("previewImg") val previewImg: String,
              @SerializedName("imagesUrl") val imageUrls: List<String>,
              @SerializedName("newsId") val newsId: String)