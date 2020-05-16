package ru.komcity.mobile.repository.mapping

import ru.komcity.mobile.repository.model.AnnouncementDto
import ru.komcity.mobile.viewModel.Announcement

/**
 * Created by Aleksei Kholoimov on 06.04.2020
 * <p>
 * Преобразует модел "Объявление" для использования на view
 */
class AnnouncementsMapper : BaseMapper<AnnouncementDto, Announcement> {
    override fun convert(item: AnnouncementDto): Announcement {
        return Announcement(item.id, item.description)
    }
}