package ru.komcity.mobile.common.messaging

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.komcity.mobile.R
import ru.komcity.mobile.network.ApiNetwork
import ru.komcity.mobile.ui.MainActivity
import kotlin.random.Random

/**
 * Created by Aleksei Kholoimov on 16.05.2020
 * <p>
 * Управление push-сообщениями
 */
private const val CHANNEL_ID = "Komcity_Channel"

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
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.vector_ic_komcity_logo)
            .setContentTitle(messageTitle)
            .setContentText(messageBody)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    },
                    getActualFlags()
                )
            )
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(
                    NotificationChannel(
                        CHANNEL_ID,
                        "pozdrav_app",
                        NotificationManager.IMPORTANCE_HIGH
                    )
                )
            }
            // notificationId is a unique int for each notification that you must define
            val notificationId = Random.nextInt(1, 10000)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    notify(notificationId, builder.build())
                }
            } else {
                notify(notificationId, builder.build())
            }
        }
    }

    private fun getActualFlags() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
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