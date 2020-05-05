package ru.komcity.mobile.common

/**
 * Created by Aleksei Kholoimov on 05.05.2020
 * <p>
 * Шаблоны для времени
 */
enum class DateTimePattern(val pattern: String) {
    DATE_TIME_ZONE("yyyy-MM-dd'T'HH:mm:ss'Z'XXX"),
    TIME_HH_MM("HH:mm"),
    DATE_YYYY_DD_MM("yyyy.MM.dd"),
    DATE_TIME_YYYY_DD_MM_HH_MM("yyyy.MM.dd HH:mm");
}