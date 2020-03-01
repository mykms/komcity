package ru.komcity.mobile.viewModel

/**
 * Created by Aleksey on 2020.03.01
 * <p>
 * Объектя для Экрана Объявлений
 */
data class AnnouncementCategory(val title: String,
                                val items: List<AnnouncementCategoryDetail>)

data class AnnouncementCategoryDetail(val name: String,
                                      val ref1: Int,
                                      val ref2: Int,
                                      val ref3: Int)

data class AnnouncementSubCategory(val title: String,
                                   val items: List<AnnouncementSubCategoryDetail>)

data class AnnouncementSubCategoryDetail(val id: String,
                                         val name: String)

data class Announcement(val id: Int,
                        val description: String)