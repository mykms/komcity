package ru.komcity.mobile.common.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.komcity.mobile.R
import ru.komcity.mobile.network.ApiNetwork
import ru.komcity.mobile.ui.MainActivity

/**
 * Created by Aleksei Kholoimov on 16.05.2020
 * <p>
 * Управление push-сообщениями
 */
class PushService: FirebaseMessagingService() {

    private val pushPresenter: PushPresenter = PushPresenter(ApiNetwork().api)

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        val data: Map<String, String> = p0.data
        p0.notification?.let {
            with(it) {
                sendNotification(title ?: "", body ?: "")
            }
        }
    }

    private fun sendNotification(messageTitle: String, messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT)

        val channelId = "Komcity_Channel"
        //val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.vector_ic_komcity_logo)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(false)
                //.setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        saveLocal(p0)
        pushPresenter.registerToken(p0)
    }

    private fun saveLocal(token: String) {
        getSharedPreferences("ru.komcity.mobile", Context.MODE_PRIVATE).apply {
            this.edit().putString("USER_ID", token).apply()
        }
    }
}