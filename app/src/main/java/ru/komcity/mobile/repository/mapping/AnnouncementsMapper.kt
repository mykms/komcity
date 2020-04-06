package ru.komcity.mobile.repository.mapping

import ru.komcity.mobile.repository.model.AnnouncementDto

/**
 * Created by Aleksei Kholoimov on 06.04.2020
 * <p>
 * Преобразует модел "Объявление" для использования на view
 */
class AnnouncementsMapper : BaseMapper<AnnouncementDto, String> {
    override fun convert(item: AnnouncementDto): String {
        return item.description
    }
}