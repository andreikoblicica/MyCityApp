package com.example.communityappmobile.services.ui.facilities

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.communityappmobile.R
import com.example.communityappmobile.apis.ServerAPI
import com.example.communityappmobile.models.auth.AuthResponse
import com.example.communityappmobile.models.auth.User
import com.example.communityappmobile.models.elements.Facility
import com.example.communityappmobile.models.elements.MainMenuOption
import com.example.communityappmobile.services.HomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FacilitiesViewModel : ViewModel() {

    private var facilities: ArrayList<Facility> =ArrayList()

    private val facilityTypes= arrayListOf("All", "Park", "Mall", "Library","Cinema","Sports Facility","Museum","Attraction")


    fun setFacilities(all:ArrayList<Facility>) {
        facilities=all
    }

    fun getFacilities(): ArrayList<Facility> {
        return facilities
    }

    fun getFacilityTypes(): ArrayList<String> {
        return facilityTypes
    }
}