package com.sharetosocial.android

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.share_to_social_view.view.*
import java.io.File

class ShareToSocial(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    private var isVisibleMessenger = false
    private var mediaFile: File? = null
    private var mediaDescription: String = ""
    private var onSocialClick: ((item: SocialApp) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.share_to_social_view, this)
        groupMessenger.isVisible = isVisibleMessenger
        initButtons()
    }

    private fun initButtons() {
        imgShare.setOnClickListener {
            groupMessenger.isVisible = !isVisibleMessenger
            it.rotation = if (isVisibleMessenger) {
                0f
            } else {
                180f
            }
            isVisibleMessenger = !isVisibleMessenger
        }
        imgTelegram.setOnClickListener { onSocialClick?.invoke(SocialApp.telegram) }
        imgWhatsapp.setOnClickListener { onSocialClick?.invoke(SocialApp.whatsapp) }
        imgVkontakte.setOnClickListener { onSocialClick?.invoke(SocialApp.vkontakte) }
        imgTwitter.setOnClickListener { onSocialClick?.invoke(SocialApp.twitter) }
        imgFacebook.setOnClickListener { onSocialClick?.invoke(SocialApp.facebook) }
        imgInstagram.setOnClickListener { onSocialClick?.invoke(SocialApp.instagram) }
    }

    private fun checkMediaFileAndShare(packageName: String) {
        setPackageToShare(packageName)
    }

    fun setOnSocialClickListener(onSocialClick: (item: SocialApp) -> Unit) {
        this.onSocialClick = onSocialClick
    }

    /**
     * Поделиться файлом и описанием
     */
    fun shareMedia(item: SocialApp, mediaFile: File?, mediaDescription: String) {
        this.mediaFile = mediaFile
        this.mediaDescription = mediaDescription
        when (item) {
            SocialApp.telegram -> {
                checkMediaFileAndShare("org.telegram.messenger")
            }
            SocialApp.whatsapp -> {
                checkMediaFileAndShare("com.whatsapp")
            }
            SocialApp.vkontakte -> {
                checkMediaFileAndShare("com.vkontakte.android")
            }
            SocialApp.twitter -> {
                checkMediaFileAndShare("com.twitter.android")
            }
            SocialApp.facebook -> {
                checkMediaFileAndShare("com.facebook.katana")
            }
            SocialApp.instagram -> {
                checkMediaFileAndShare("com.instagram.android")
            }
            else -> Unit
        }
    }

    private fun setPackageToShare(packageName: String) {
        if (isAppAvailable(context, packageName)) {
            shareToApp(packageName)
        } else {
            //Уведомление, что не установлено приложение
            Toast.makeText(context, "Выбранное приложение не установлено", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    /**
     * Проверяет установлено ли приложение с указанным именем пакета
     * @param context Контекст
     * @param appName Имя пакета для поиска
     * @return Результат поиска. true - если такой пакет есть, иначе false
     */
    private fun isAppAvailable(context: Context, appName: String): Boolean = try {
        context.packageManager.getPackageInfo(appName, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }

    private fun shareToApp(packageName: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, mediaDescription)
            type = "text/plain"
            setPackage(packageName)
        }
        addFileInfoIfExist(shareIntent)
        context.startActivity(Intent.createChooser(shareIntent, "Поделиться в"))
    }

    private fun addFileInfoIfExist(intent: Intent): Intent {
        return mediaFile?.let {
            try {
                if (mediaFile?.exists() == true) {
                    val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mediaFile?.let { FileProvider.getUriForFile(context, context.packageName + ".provider", it) }
                    } else {
                        Uri.fromFile(mediaFile)
                    }
                    intent.apply {
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        type = "image/*"
                        putExtra(Intent.EXTRA_STREAM, uri)
                    }
                } else {
                    intent
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                intent
            }
        } ?: intent
    }
}