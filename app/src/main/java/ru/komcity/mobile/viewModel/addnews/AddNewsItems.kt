package ru.komcity.mobile.viewModel.addnews

import java.io.File

/**
 * Created by Aleksei Kholoimov on 10.05.2020
 * <p>
 * Элемент в списке для добавления новости
 */
interface AddNewsBaseItem

/**
 * Элемент с прикрепленным файлом
 */
class AddNewsItemFile(val fileType: String, val file: File): AddNewsBaseItem

/**
 * Элемент без файла
 */
class AddNewsItemEmpty: AddNewsBaseItem