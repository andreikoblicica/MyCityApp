package com.example.communityappmobile.services

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.communityappmobile.R
import com.example.communityappmobile.databinding.ActivityHomeBinding
import com.example.communityappmobile.models.auth.User
import com.example.communityappmobile.services.ui.alerts.AlertsFragment
import com.example.communityappmobile.services.ui.home.HomeFragment
import com.example.communityappmobile.services.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging



class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityHomeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance()
            .subscribeToTopic("community_app_notification")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Subscribed to topic successfully")
                } else {
                    Log.e("FCM", "Failed to subscribe to topic", task.exception)
                }
            }

        FirebaseMessaging.getInstance().subscribeToTopic(User.username)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Subscribed to topic successfully")
                } else {
                    Log.e("FCM", "Failed to subscribe to topic", task.exception)
                }
            }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home)


        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_alerts, R.id.navigation_home,R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setOnItemSelectedListener {
            when(it.toString()){
                "Home" -> {
                    navController.popBackStack(R.id.navigation_home,false)
                    navController.navigate(R.id.navigation_home)
                }
                "Profile" -> {
                    navController.popBackStack(R.id.navigation_home,false)
                    navController.navigate(R.id.navigation_profile)
                }
                "Alerts" -> {
                    navController.popBackStack(R.id.navigation_home,false)
                    navController.navigate(R.id.navigation_alerts)
                }
            }
            return@setOnItemSelectedListener true

        }

    }

    override fun onBackPressed() {
        val navFragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_home)

        val currentFragment= navFragment?.childFragmentManager?.primaryNavigationFragment
        if (currentFragment is HomeFragment || currentFragment is ProfileFragment || currentFragment is AlertsFragment) {
            finishAffinity()
        } else {
            super.onBackPressed()
        }
    }

}


