package com.example.communityappmobile.services.ui.home


import androidx.lifecycle.ViewModel
import com.example.communityappmobile.R
import com.example.communityappmobile.models.elements.MainMenuOption


class HomeViewModel : ViewModel() {

    private var menuOptions: ArrayList<MainMenuOption> =ArrayList()

    init {
        menuOptions.add(MainMenuOption(R.drawable.train4,R.drawable.alert, "Issues"))
        menuOptions.add(MainMenuOption(R.drawable.facility3,R.drawable.facilityicon, "Facilities"))
        menuOptions.add(MainMenuOption(R.drawable.clujj3,R.drawable.institutionicon, "Institutions"))
        menuOptions.add(MainMenuOption(R.drawable.event4,R.drawable.eventicon, "Events"))
        menuOptions.add(MainMenuOption(R.drawable.news2,R.drawable.newsicon, "News"))
    }

    fun getMenuOptions(): ArrayList<MainMenuOption> {
        return menuOptions
    }
}