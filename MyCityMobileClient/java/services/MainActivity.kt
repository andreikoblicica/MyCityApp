package com.example.communityappmobile.services

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.communityappmobile.R
import com.example.communityappmobile.databinding.ActivityMainBinding
import com.example.communityappmobile.models.auth.SharedPreferencesHelper
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapsSdkInitializedCallback


class MainActivity : AppCompatActivity(),OnMapsSdkInitializedCallback {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapsInitializer.initialize(applicationContext,MapsInitializer.Renderer.LATEST,this)

        if (SharedPreferencesHelper.isLoggedIn(this)) {
            SharedPreferencesHelper.getUser(this)
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)
        } else {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            navController.navigate(R.id.navigation_log_in)
            }


    }
    

    override fun onMapsSdkInitialized(renderer: MapsInitializer.Renderer) {
        when (renderer) {
            MapsInitializer.Renderer.LATEST -> Log.d(
                "MapsDemo",
                "The latest version of the renderer is used."
            )
            MapsInitializer.Renderer.LEGACY -> Log.d(
                "MapsDemo",
                "The legacy version of the renderer is used."
            )
        }
    }
}