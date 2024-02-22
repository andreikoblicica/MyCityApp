package com.example.communityappmobile.services.ui.events

import androidx.lifecycle.ViewModel

import com.example.communityappmobile.models.events.Event

class EventsViewModel : ViewModel() {

    private var events: ArrayList<Event> =ArrayList()

    private val eventTypes = arrayListOf(
        "All",
        "Concert",
        "Party",
        "Festival",
        "Show",
        "Sports",
        "Exhibition",
        "Book Launch",
        "Social",
        "Educational",
        "Business",
        "Movie",
        "Diverse"
    )


    fun setEvents(all:ArrayList<Event>) {
        events=all
    }


    fun getEventTypes():ArrayList<String>{
        return eventTypes
    }
}