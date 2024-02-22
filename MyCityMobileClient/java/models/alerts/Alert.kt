package com.example.communityappmobile.models.alerts

data class Alert(
    var id: Long,
    var institution: String,
    var title: String,
    var description: String,
    var dateTime: String
):java.io.Serializable
