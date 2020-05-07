package ru.komcity.mobile.network

/**
 * Created by Aleksei Kholoimov on 29.04.2020
 * <p>
 * Хранит данные для соединения с сервером
 */
data class MailSenderData(val emailFrom: String,
                          val userLogin: String,
                          val userPwd: String,
                          val serverHost: String,
                          val serverPort: String)