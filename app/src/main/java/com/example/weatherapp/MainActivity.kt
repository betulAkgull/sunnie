package com.example.weatherapp

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.weatherapp.common.gone
import com.example.weatherapp.common.viewBinding
import com.example.weatherapp.common.visible
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.ui.login.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val viewModel by viewModels<AuthViewModel>()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { }
        permissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        with(binding) {

            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            NavigationUI.setupWithNavController(navView, navHostFragment.navController)


            val toggle = ActionBarDrawerToggle(this@MainActivity, drawerLayout, toolbar, 0, 0)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.homeFragment) {
                    toolbar.visible()
                } else {
                    toolbar.gone()
                }
            }

            layoutUserInfo.ivLogout.setOnClickListener {
                viewModel.logout()
                navHostFragment.findNavController().navigate(R.id.splashFragment)
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            viewModel.currentUser?.let {
                layoutUserInfo.tvUserEmail.text = viewModel.currentUser?.email.toString()
            }

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