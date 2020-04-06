package ru.komcity.mobile.repository.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Aleksei Kholoimov on 15.03.2020
 * <p>
 * Объекты для Объявлений
 */
class AnnouncementCategoryDto(@SerializedName("title") val title: String,
                              @SerializedName("items") val items: List<AnnouncementCategoryDetailDto>)

class AnnouncementCategoryDetailDto(@SerializedName("name") val name: String,
                                    @SerializedName("ref1") val ref1: Int,
                                    @SerializedName("ref2") val ref2: Int,
                                    @SerializedName("ref3") val ref3: Int)

data class AnnouncementSubCategoryDto(@SerializedName("title") val title: String,
                                      @SerializedName("items") val items: List<AnnouncementSubCategoryDetailDto>)

data class AnnouncementSubCategoryDetailDto(@SerializedName("id") val id: String,
                                            @SerializedName("name") val name: String)

class AnnouncementDto(@SerializedName("id") val id: Int,
                      @SerializedName("description") val description: String)