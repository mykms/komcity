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
import androidx.core.content.FileProvider
import java.io.File

class ShareToSocial(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
    private val shareIntent = Intent(Intent.ACTION_SEND)
    private var imgPath = ""
    private var textForShare = ""
//    @BindView(R.id.group_messenger)    var groupMessenger: LinearLayout? = null
//    @BindView(R.id.img_share)    var imgShare: ImageView? = null


    init {
        LayoutInflater.from(context).inflate(R.layout.share_to_social_view, this)
        //groupMessenger!!.visibility = View.GONE // Default - invisible
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
    private fun isAppAvailable(context: Context, appName: String): Boolean {
        val pm = context.packageManager
        try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }

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

//    @OnClick(R.id.img_share)
//    fun OnShareClick() {
//        var resId = R.drawable.vector_ic_arrow_back
//        if (groupMessenger!!.visibility == View.GONE) {
//            resId = R.drawable.vector_ic_arrow_next
//            groupMessenger!!.visibility = View.VISIBLE
//        } else {
//            resId = R.drawable.vector_ic_arrow_back
//            groupMessenger!!.visibility = View.GONE
//        }
//        imgShare!!.setImageDrawable(resources.getDrawable(resId))
//    }
//
//    @OnClick(R.id.img_instagram)
//    fun OnClick_instagram() {
//        setPackageToShare("com.instagram.android")
//    }
//
//    @OnClick(R.id.img_facebook)
//    fun OnClick_facebook() {
//        setPackageToShare("com.facebook.katana")
//    }
//
//    @OnClick(R.id.img_twitter)
//    fun OnClick_twitter() {
//        setPackageToShare("com.twitter.android")
//    }
//
//    @OnClick(R.id.img_vkontakte)
//    fun OnClick_vkontakte() {
//        setPackageToShare("com.vkontakte.android")
//    }
//
//    @OnClick(R.id.img_whatsapp)
//    fun OnClick_whatsapp() {
//        setPackageToShare("com.whatsapp")
//    }
//
//    @OnClick(R.id.img_telegram)
//    fun OnClick_telegram() {
//        setPackageToShare("org.telegram.messenger")
//    }
}