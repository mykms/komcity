package ru.komcity.mobile.network

import java.io.File
import java.util.*
import javax.activation.DataHandler
import javax.activation.DataSource
import javax.activation.FileDataSource
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

/**
 * Created by Aleksei Kholoimov on 27.04.2020
 * <p>
 * Sending E-mail
 * @param params - Параметры, необходимые для корректной отправки письма
 */
class MailSender(private val params: MailSenderData) {

    private val emailTo = "admin@komcity.ru"
    private lateinit var session: Session

    init {
        val props = initProperties(params)
        session = initSession(params, props)
    }

    private fun initProperties(params: MailSenderData) = Properties().apply {
        put("mail.smtp.host", params.serverHost)
        put("mail.smtp.ssl.enable", "true") // Если почтовый сервер использует SSL
        put("mail.smtp.starttls.enable", "true")
        put("mail.smtp.socketFactory.port", "465")
        put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
        put("mail.smtp.socketFactory.fallback", "false")
        put("mail.smtp.ssl.trust", "*") // Если требуется сертификат
        put("mail.smtp.port", params.serverPort) // Указываем порт SMTP сервера.
        put("mail.smtp.auth", "true") // Большинство SMTP серверов, используют авторизацию.
        put("mail.debug", "false") // использовалось для отладки
        put("mail.smtp.connectiontimeout", 10 * 1000)
        put("mail.smtp.timeout", 10 * 1000)
    }

    private fun initSession(params: MailSenderData, props: Properties): Session {
        return Session.getDefaultInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                // Указываем логин пароль, от почты, с которой будем отправлять сообщение.
                return PasswordAuthentication(params.userLogin, params.userPwd)
            }
        })
    }

    /**
     * Отправляет письмо
     * @param subject - Тема письма
     * @param text - Основное содержание письма
     * @param attachFiles - Приложенные файлы (если имеются) или пусто, если их нет
     * @param onMailResult - Результат отправки письма
     */
    fun sendMail(subject: String,
                 text: String,
                 attachFiles: List<File> = emptyList(),
                 onMailResult: (isSuccess: Boolean, message: String) -> Unit) {
        val message = createMessage(subject, text, attachFiles)
        sendMessageByTransport(message, onMailResult)
    }

    private fun createMessage(subject: String, text: String, attachFiles: List<File> = emptyList()): Message {
        return MimeMessage(session).apply {
            setFrom(InternetAddress(params.emailFrom))
            setRecipients(Message.RecipientType.TO, arrayOf(InternetAddress(emailTo)))
            setSubject(subject)
            sentDate = Date()
            setText(text)
            setContent(getAttaches(attachFiles))
        }
    }

    private fun getAttaches(attachFiles: List<File>): Multipart {
        return MimeMultipart().apply {
            for (item in attachFiles) {
                val messageBodyPart = MimeBodyPart().apply {
                    val source: DataSource = FileDataSource(item.path)
                    dataHandler = DataHandler(source)
                    fileName = item.name
                }
                addBodyPart(messageBodyPart)
            }
        }
    }

    private fun sendMessageByTransport(message: Message, onMailResult: (isSuccess: Boolean, message: String) -> Unit) {
        val transport: Transport = session.getTransport("smtp")
        try {
            transport.connect()
            transport.sendMessage(message, message.allRecipients)
        } catch (e: MessagingException) {
            onMailResult(false, e.message ?: "")
        } finally {
            transport.close()
            onMailResult(true, "")
        }
    }
}