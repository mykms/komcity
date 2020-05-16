package ru.komcity.mobile.ui.fragment

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.android.synthetic.main.fragment_info.*
import ru.komcity.mobile.BuildConfig
import ru.komcity.mobile.R

class InfoFragment : BaseFragment() {

    override fun onCreateInit(clientId: String, context: Context) {
    }

    override fun getArgs(args: Bundle?) {
        //
    }

    override fun setResourceLayout(): Int {
        return R.layout.fragment_info
    }

    override fun initComponents(view: View) {
        tvVersion.text = "Версия: ${BuildConfig.VERSION_NAME}"
        tvShareApp.setOnClickListener {
            shareMyApplication(it.context)
        }
        tvRateAndReview.setOnClickListener {
            retaWithPlayMarket(it.context)
        }
        tvClose.setOnClickListener {
            activity?.finish()
        }
    }

    private fun shareMyApplication(context: Context) {
        try {
            val pkg = context.packageName
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Мобильное приложение Komcity.ru под Android")
                putExtra(Intent.EXTRA_TEXT, "Рекомендую\nМобильное приложение Komcity.ru под Android\nhttps://play.google.com/store/apps/details?id=$pkg\n\n")
            }
            startActivity(Intent.createChooser(shareIntent, "Выберите где хотите поделиться"))
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log(e.message ?: "crash shareMyApplication")
        }
    }

    private fun retaWithPlayMarket(context: Context) {
        val pkg = context.packageName
        val uri: Uri = Uri.parse("market://details?id=$pkg")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            addFlags(if (Build.VERSION.SDK_INT >= 21) {
                flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
            } else {
                flags or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
            })
        }
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        // To count with Play market backstack, After pressing back button, to taken back to our application, we need to add following flags to intent.
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=$pkg")))
        }
    }
}