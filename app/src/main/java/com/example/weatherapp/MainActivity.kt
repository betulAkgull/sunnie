package com.example.weatherapp

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.weatherapp.common.viewBinding
import com.example.weatherapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {

            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            NavigationUI.setupWithNavController(navView, navHostFragment.navController)


            val toggle = ActionBarDrawerToggle(this@MainActivity, drawerLayout, toolbar, 0, 0)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()


            val header = navView.inflateHeaderView(R.layout.nav_view_header)
           // header.findViewById<TextView>(R.id.tv_title).text = "Header"

            onBackPressedDispatcher.addCallback(
                this@MainActivity,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                            drawerLayout.closeDrawer(GravityCompat.START)
                        } else if (navHostFragment.navController.previousBackStackEntry == null) {
                            finish()
                        } else {
                            navHostFragment.navController.navigateUp()
                        }
                    }
                })
        }
    }
}