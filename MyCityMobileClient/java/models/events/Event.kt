package com.example.communityappmobile.models.events

data class Event (
    val id: Long,
    val username: String,
    val type: String,
    val title: String,
    val image: String,
    val startDateTime: String,
    val endDateTime: String,
    val description: String,
    val location: String,
    val status: String,
    val favoriteCount: Long
        ):java.io.Serializable

