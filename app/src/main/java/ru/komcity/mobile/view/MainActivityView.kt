package ru.komcity.mobile.view

import android.os.Bundle

interface MainActivityView {
    fun navigateTo(resourceFragment: Int, args: Bundle?)
    fun navigateToBack()
    fun getClientId(): String
    fun onMessage(message: String)
    fun isBottomPanelVisible(isVisible: Boolean)
}