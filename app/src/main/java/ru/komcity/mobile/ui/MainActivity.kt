package ru.komcity.mobile.ui

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*
import ru.komcity.mobile.R
import ru.komcity.mobile.view.MainActivityView

class MainActivity: AppCompatActivity(), MainActivityView {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initNavigation()
    }

    private fun initNavigation() {
        navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupWithNavController(bottomNavigation, navController)
        bottomNavigation.setOnNavigationItemSelectedListener {
            item -> navigateApp(navController, item.itemId, Bundle())
        }
    }

    private fun navigateApp(navController: NavController, @IdRes resourceIdNav: Int, args: Bundle): Boolean {
        when(resourceIdNav) {
            R.id.newsListFragment -> {
                //args.putBoolean(Constants.EXTRA_IS_PANEL_VISIBLE, isVisibleBottomPanel())
                navController.navigate(resourceIdNav, args)
            }
            R.id.newsDetailFragment -> navController.navigate(resourceIdNav, args)
            R.id.forumListFragment -> navController.navigate(resourceIdNav, args)
            R.id.forumSubListDetailFragment -> navController.navigate(resourceIdNav, args)
            R.id.forumDetailMessageFragment -> navController.navigate(resourceIdNav, args)
            R.id.announcementFilterFragment -> navController.navigate(resourceIdNav, args)
            R.id.announcementsFragment -> navController.navigate(resourceIdNav, args)
            android.R.id.home -> navController.popBackStack()
            else -> navController.navigate(R.id.noContentErrorFragment)
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

    override fun onMessage(message: String) {
        //
    }
}