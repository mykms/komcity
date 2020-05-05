package ru.komcity.mobile.common

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Aleksei Kholoimov on 05.05.2020
 * <p>
 * Помощник для работы с датой и временем
 */
class DateTimeUtils {

    companion object {
        fun toHumanDateTime(dateTime: Calendar): String {
            val wordFormatter = SimpleDateFormat(DateTimePattern.TIME_HH_MM.pattern, Locale.getDefault())
            val otherFormatter = SimpleDateFormat(DateTimePattern.DATE_TIME_YYYY_DD_MM_HH_MM.pattern, Locale.getDefault())
            return when {
                isToday(dateTime) -> {
                    "Сегодня в ${wordFormatter.format(dateTime.time)}"
                }
                isYesterday(dateTime) -> {
                    "Вчера в ${wordFormatter.format(dateTime.time)}"
                }
                else -> {
                    otherFormatter.format(dateTime.time)
                }
            }
        }

        private fun isToday(dateTime: Calendar): Boolean {
            val today = resetTime(Calendar.getInstance(TimeZone.getDefault()))
            val otherDate = Calendar.getInstance().apply {
                timeInMillis = dateTime.timeInMillis
            }
            return today.compareTo(resetTime(otherDate)) == 0
        }

        private fun isYesterday(dateTime: Calendar): Boolean {
            val yesterday = resetTime(Calendar.getInstance(TimeZone.getDefault())).apply {
                add(Calendar.DAY_OF_MONTH, -1)
            }
            val otherDate = Calendar.getInstance().apply {
                timeInMillis = dateTime.timeInMillis
            }
            return yesterday.compareTo(resetTime(otherDate)) == 0
        }

        private fun resetTime(dateTime: Calendar): Calendar {
            return dateTime.apply {
                set(Calendar.MILLISECOND, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.HOUR_OF_DAY, 0)
            }
        }
    }
}