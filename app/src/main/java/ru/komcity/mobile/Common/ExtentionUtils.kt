package ru.komcity.mobile.Common

import java.text.SimpleDateFormat
import java.util.*

fun Calendar?.toPattern(pattern: String = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(this?.time)
}

fun String?.toCalendar(pattern: String = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"): Calendar {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    val parsedDate = Calendar.getInstance()
    return try {
        parsedDate.time = formatter.parse(this)
        parsedDate
    } catch (ex: Exception) {
        parsedDate.set(Calendar.YEAR, 1800)
        parsedDate
    }
}

fun Calendar.toUserFriendly(): String = when {
    isToday(this) -> "Сегодня в " + this.toPattern("HH:mm")
    isYesterday(this) -> "Вчера в " + this.toPattern("HH:mm")
    else -> this.toPattern("HH:mm dd MMMM yyyy")
}

private fun isToday(otherDate: Calendar): Boolean {
    val today = Calendar.getInstance()
    return isEqualDays(today, otherDate)
}

private fun isYesterday(otherDate: Calendar): Boolean {
    val yesterday = Calendar.getInstance()
    yesterday.add(Calendar.DAY_OF_WEEK, -1)
    return isEqualDays(yesterday, otherDate)
}

private fun isTomorrow(otherDate: Calendar): Boolean {
    val tomorrow = Calendar.getInstance()
    tomorrow.add(Calendar.DAY_OF_WEEK, 1)
    return isEqualDays(tomorrow, otherDate)
}

private fun isEqualDays(mainDate: Calendar, otherDate: Calendar): Boolean {
    return mainDate.get(Calendar.YEAR) == otherDate.get(Calendar.YEAR) &&
            mainDate.get(Calendar.MONTH) == otherDate.get(Calendar.MONTH) &&
            mainDate.get(Calendar.DAY_OF_MONTH) == otherDate.get(Calendar.DAY_OF_MONTH)
}