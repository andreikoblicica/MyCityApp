package com.example.communityappmobile.models.issue

import java.io.Serializable

data class Message(
    val issueId: Long,
    val sourceName: String,
    val destinationNames: ArrayList<String>,
    val message: String,
    val timestamp: String
): Serializable
