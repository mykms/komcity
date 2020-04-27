package ru.komcity.mobile.network

import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

/**
 * Created by Aleksei Kholoimov on 27.04.2020
 * <p>
 * Sending E-mail
 */
class MailSender {

    private fun SendInfoToEmail(params: Array<String>, _subject: String, _text: String): Boolean {
        val to = params[0] //"test201701@yandex.ru";//e-mail
        val host = params[1] //"smtp.yandex.com";//smtp host;
        val user = params[2] //"test201701";//login;
        val password = params[3] //"123456789q";//password;
        val port = Integer.valueOf(params[4]) //smtp port(456);
        // Сюда необходимо подставить адрес получателя сообщения
        // Создание свойств, получение сессии
        val props = Properties()
        // При использовании статического метода Transport.send() необходимо указать через какой хост будет передано сообщение
        props.put("mail.smtp.host", host)
        props.put("mail.smtp.ssl.enable", "true") // Если почтовый сервер использует SSL
        props.put("mail.smtp.starttls.enable", "true")
        props.put("mail.smtp.socketFactory.port", "465")
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
        props.put("mail.smtp.socketFactory.fallback", "false")
        props.put("mail.smtp.ssl.trust", "*") // Если требуется сертификат
        props.put("mail.smtp.port", port) // Указываем порт SMTP сервера.
        props.put("mail.smtp.auth", "true") // Большинство SMTP серверов, используют авторизацию.
        props.put("mail.debug", "false") // использовалось для отладки
        props.put("mail.smtp.connectiontimeout", 10 * 1000)
        props.put("mail.smtp.timeout", 10 * 1000)
        // Авторизируемся.
        val session: Session = Session.getDefaultInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                // Указываем логин пароль, от почты, с которой будем отправлять сообщение.
                return PasswordAuthentication(user, password)
            }
        })
        try { // Создание объекта сообщения
            val msg: Message = MimeMessage(session)
            // Установка атрибутов сообщения
            msg.setFrom(InternetAddress(to))
            val address = arrayOf(InternetAddress(to))
            msg.setRecipients(Message.RecipientType.TO, address)
            msg.setSubject(_subject)
            msg.setSentDate(Date())
            // Установка тела сообщения
            msg.setText(_text)
            // Отправка сообщения
//Transport.send(msg);
            val transport: Transport = session.getTransport("smtp")
            try {
                transport.connect()
                transport.sendMessage(msg, msg.getAllRecipients())
            } catch (e: MessagingException) {
                e.getStackTrace()
                return false
            } finally {
                transport.close()
                return true
            }
        } catch (mex: MessagingException) { // Печать информации об исключении в случае его возникновения
            mex.printStackTrace()
            return false
        }
    }
}