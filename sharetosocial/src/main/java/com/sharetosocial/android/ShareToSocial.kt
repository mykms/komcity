package com.sharetosocial.android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.share_to_social_view.view.*
import java.io.File

class ShareToSocial : RelativeLayout {
    private val shareIntent = Intent(Intent.ACTION_SEND)
    private var imgPath = ""
    private var textForShare = ""
    private var isVisibleMessenger = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        LayoutInflater.from(context).inflate(R.layout.share_to_social_view, this)
        groupMessenger.isVisible = isVisibleMessenger
        initButtons()
    }

    private fun initButtons() {
        imgShare.setOnClickListener {
            groupMessenger.isVisible = isVisibleMessenger
            imgShare.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.vector_ic_arrow_back))
            if (isVisibleMessenger) {
                imgShare.rotation = 0f
            } else {
                imgShare.rotation = 180f
            }
            isVisibleMessenger = !isVisibleMessenger
        }
        imgTelegram.setOnClickListener { setPackageToShare("org.telegram.messenger") }
        imgWhatsapp.setOnClickListener { setPackageToShare("com.whatsapp") }
        imgVkontakte.setOnClickListener { setPackageToShare("com.vkontakte.android") }
        imgTwitter.setOnClickListener { setPackageToShare("com.twitter.android") }
        imgFacebook.setOnClickListener { setPackageToShare("com.facebook.katana") }
        imgInstagram.setOnClickListener { setPackageToShare("com.instagram.android") }
    }

    /**
     * Установка размером кастомной вьюхи
     * @param size Размер в dp
     */
    fun setViewSize(size: Int) {
        var size = size
        if (size < 0)
            size = 1
        if (size > 0) {
//            if (imgShare != null) {
//                val params = imgShare!!.layoutParams
//                val newsize = convertDptoPX(size).toInt()
//                params.height = newsize
//                params.width = newsize
//                imgShare!!.layoutParams = params
//            }
        }
    }

    /**
     * Устанавливает полный путь к изображению, которым нужно поделиться
     * @param img_path Полный путь к изображению
     */
    fun setImagePathToShare(img_path: String?) {
        var img_path = img_path
        if (img_path == null)
            img_path = ""
        imgPath = img_path
    }

    /**
     * Преобразование dp в px
     * @param valueDP Значение в dp
     * @return Возвращает значение в px
     */
    private fun convertDptoPX(valueDP: Int): Float {
        var valueDP = valueDP
        if (valueDP < 0)
            valueDP = 0
        val r = resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueDP.toFloat(), r.displayMetrics)
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

    /**
     * Устанавливает рисунок в виде bmp-данных, чтобы им поделиться
     * @param bmp Рисунок, представленный Bitmap данными
     * @param fName Путь для сохранения. Если null, то по умолчанию
     */
    fun setBitmapToShare(activityForPermission: Activity, bmp: Bitmap?, fName: String?) {
        var bmp = bmp
        var fName = fName
        if (bmp == null) {
            // Создаем рисунок по умолчанию (логотип)
            //bmp = BitmapFactory.decodeResource(resources, R.drawable.vector_ic_news)

            val matrix = Matrix()
            matrix.postScale(10f, 15f)
            matrix.postRotate(45f)
        }

//        val utils = Utils()
//        if (fName == null) {
//            fName = "img.jpg"
//        }
//        if (utils.saveImageLocal(activityForPermission, bmp, fName)) {
//            imgPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() +
//                    File.separator +
//                    fName
//        }
    }

    fun setTextForShare(text: String?) {
        var text = text
        if (text == null)
            text = ""
        textForShare = text
    }

    private fun shareToApp() {
        val type = "image/*"
        val typeText = "text/plain"

        val media = File(imgPath)

        try {
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            shareIntent.type = type
            if (media.exists()) {
                var uri = Uri.fromFile(media)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(context, context.packageName + ".provider", media)
                }
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            }
            shareIntent.putExtra(Intent.EXTRA_TEXT, textForShare)
            context.startActivity(Intent.createChooser(shareIntent, "Поделиться в"))
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    private fun setPackageToShare(packageName: String) {
        if (isAppAvailable(context, packageName)) {
            shareIntent.setPackage(packageName)
            shareToApp()
        } else {
            //Уведомление, что не установлено приложение
            Toast.makeText(context,
                    "Выбранное приложение не установлено",
                    Toast.LENGTH_SHORT).show()
        }
    }
}