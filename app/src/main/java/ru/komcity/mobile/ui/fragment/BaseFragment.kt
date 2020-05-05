package ru.komcity.mobile.ui.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import moxy.MvpAppCompatFragment
import moxy.MvpView
import ru.komcity.mobile.view.MainActivityView
import ru.komcity.mobile.viewModel.User
import java.util.*

abstract class BaseFragment : MvpAppCompatFragment(), MvpView {

    private var activityListener: MainActivityView? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivityView) {
            activityListener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgs(arguments)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(setResourceLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents(view)
    }

    protected fun navigateTo(@IdRes resourceFragment: Int, args: Bundle?) {
        activityListener?.navigateTo(resourceFragment, args)
    }

    protected fun navigateToBack() {
        activityListener?.navigateToBack()
    }

    protected fun hideKeyboard() {
        val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity?.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun onMessage(message: String) {
        activityListener?.onMessage(message)
    }

    /**
     * Устанавливает видимость панели внизу
     */
    protected fun setBottomPanelVisibility(isVisible: Boolean) {
        //activityListener?.showBottomPanel(isVisible)
    }

    protected fun setUser(user: User) {
        //activityListener?.setUser(user)
    }

    protected fun setUserRole(role: String) {
        //activityListener?.setUserRole(role)
    }

    protected fun logoutUser() {
        //activityListener?.logoutUser()
    }

    protected fun getDefaultLocale(): String {
        return Locale.getDefault().language.toLowerCase()
    }

    override fun onDetach() {
        super.onDetach()
        activityListener = null
    }

    abstract fun getArgs(args: Bundle?)
    /**
     * Устанавливает разметку
     */
    abstract fun setResourceLayout(): Int

    /**
     * В это методе можно инициализировать свои компоненты
     */
    abstract fun initComponents(@NonNull view: View)
}