package com.kosim97.mulgaTalkTalk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kosim97.mulgaTalkTalk.util.NavigationUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationUtil {
    lateinit var navView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        initNavi()
    }

    private fun initNavi() {
        navView = findViewById(R.id.nav_bottom)
        val navController = findNavController(R.id.nav_host)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_search, R.id.navigation_favorites)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    override fun visibleNav(visible: Boolean) {
        if (!visible) {
            navView.visibility = View.GONE
        } else {
            navView.visibility = View.VISIBLE
        }
    }
}