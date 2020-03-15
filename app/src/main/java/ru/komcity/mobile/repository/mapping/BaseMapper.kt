package ru.komcity.mobile.repository.mapping

/**
 * Created by Aleksei Kholoimov on 15.03.2020
 * <p>
 * Преобразует элемент F в элемент T
 */
interface BaseMapper<F, T> {
    fun convert(item: F): T
}