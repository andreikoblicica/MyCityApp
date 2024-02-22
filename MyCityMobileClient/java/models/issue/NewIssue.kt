package com.example.communityappmobile.models.issue


data class NewIssue(
    val username: String,
    val type: String,
    val title: String,
    val description: String,
    val location: String,
    val coordinates: String,
    val image: String,
):java.io.Serializable
