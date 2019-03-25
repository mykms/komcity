package ru.komcity.mobile.UI

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.komcity.mobile.R
import ru.komcity.mobile.View.MainActivityView

class MainActivity: AppCompatActivity(), MainActivityView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun navigateTo(resourceFragment: Int, args: Bundle?) {
        //
    }

    override fun onMessage(message: String) {
        //
    }
}