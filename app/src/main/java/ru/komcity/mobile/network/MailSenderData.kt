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

/*
val to = params[0] //"test201701@yandex.ru";//e-mail
        val host = params[1] //"smtp.yandex.com";//smtp host;
        val user = params[2] //"test201701";//login;
        val password = params[3] //"123456789q";//password;
        val port = Integer.valueOf(params[4]) //smtp port(456);
 */