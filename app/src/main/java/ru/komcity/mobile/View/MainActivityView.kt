package ru.komcity.mobile.View

import android.os.Bundle

interface MainActivityView {
    fun navigateTo(resourceFragment: Int, args: Bundle?)
    fun onMessage(message: String)
}