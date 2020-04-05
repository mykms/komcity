package ru.komcity.mobile.repository.mapping

import ru.komcity.mobile.repository.model.AnnouncementCategoryDto
import ru.komcity.mobile.repository.model.AnnouncementSubCategoryDto
import ru.komcity.mobile.viewModel.AnnouncementCategory
import ru.komcity.mobile.viewModel.AnnouncementCategoryDetail
import ru.komcity.mobile.viewModel.AnnouncementSubCategory
import ru.komcity.mobile.viewModel.AnnouncementSubCategoryDetail

/**
 * Created by Aleksei Kholoimov on 15.03.2020
 * <p>
 * Convert AnnouncementCategory DTO to ViewModel
 */
class AnnouncementCategoryMapper: BaseMapper<AnnouncementCategoryDto, AnnouncementCategory> {

    override fun convert(item: AnnouncementCategoryDto): AnnouncementCategory = with(item) {
        AnnouncementCategory(title, items.map { AnnouncementCategoryDetail(it.name, it.ref1, it.ref2, it.ref3) })
    }

    fun convert(item: AnnouncementSubCategoryDto): AnnouncementSubCategory = with(item) {
        AnnouncementSubCategory(title, items.map { AnnouncementSubCategoryDetail(it.id, it.name) })
    }
}