package ru.komcity.mobile.ui

import android.content.Context
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import ru.komcity.mobile.R
import ru.komcity.mobile.common.messaging.PushPresenter
import ru.komcity.mobile.network.ApiNetwork
import ru.komcity.mobile.view.MainActivityView

class MainActivity: AppCompatActivity(), MainActivityView {

    private lateinit var navController: NavController
    private val pushPresenter: PushPresenter = PushPresenter(ApiNetwork().api)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        updatePushToken()
        initNavHostFragment()
        initNavigation()
    }

    private fun updatePushToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val pushToken = task.result?.token ?: ""
                pushPresenter.registerToken(pushToken)
            }
        }
    }

    private fun initNavHostFragment() {
        navController = Navigation.findNavController(this, R.id.navHostFragment).apply {
            val graph: NavGraph = navInflater.inflate(R.navigation.nav_res)
            setGraph(graph)
        }
    }

    private fun initNavigation() {
        NavigationUI.setupWithNavController(bottomNavigation, navController)
        bottomNavigation.setOnNavigationItemSelectedListener {
            item -> navigateApp(navController, item.itemId, Bundle())
        }
    }

    private fun navigateApp(navController: NavController, @IdRes resourceIdNav: Int, args: Bundle): Boolean {
        when(resourceIdNav) {
            R.id.newsListFragment -> {
                navController.navigate(resourceIdNav, args)
            }
            R.id.newsDetailFragment -> navController.navigate(resourceIdNav, args)
            R.id.forumListFragment -> navController.navigate(resourceIdNav, args)
            R.id.forumSubListDetailFragment -> navController.navigate(resourceIdNav, args)
            R.id.forumDetailMessageFragment -> navController.navigate(resourceIdNav, args)
            R.id.announcementFilterFragment -> navController.navigate(resourceIdNav, args)
            R.id.announcementsFragment -> navController.navigate(resourceIdNav, args)
            R.id.newsAdd -> navController.navigate(resourceIdNav, args)
            R.id.imageViewFragment -> navController.navigate(resourceIdNav, args)
            R.id.infoFragment -> navController.navigate(resourceIdNav, args)
            android.R.id.home -> navController.popBackStack()
            else -> navController.navigate(R.id.connectionErrorFragment)
        }
        //invalidateOptionsMenu()
        return true
    }

    override fun navigateTo(resourceFragment: Int, args: Bundle?) {
        navigateApp(navController, resourceFragment, args ?: Bundle())
    }

    override fun navigateToBack() {
        this.onBackPressed()
    }

    override fun getClientId(): String {
        var userId = ""
        getSharedPreferences("ru.komcity.mobile", Context.MODE_PRIVATE).apply {
            userId = getString("USER_ID", "") ?: ""
        }
        return userId
    }

    override fun onMessage(message: String) {
        Snackbar.make(bottomNavigation, message, Snackbar.LENGTH_SHORT)
                .show()
    }

    override fun isBottomPanelVisible(isVisible: Boolean) {
        bottomNavigation.isVisible = isVisible
    }
}