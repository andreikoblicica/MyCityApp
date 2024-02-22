package com.example.communityappmobile.models.news

data class News(
    val id:Long,
    val title: String,
    val institution: String,
    val dateTime: String,
    val description: String,
    val websiteLink: String,
    val image:String
):java.io.Serializable
