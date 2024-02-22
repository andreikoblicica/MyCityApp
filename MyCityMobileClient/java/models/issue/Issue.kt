package com.example.communityappmobile.models.issue

import android.os.Parcelable
import java.util.*

data class Issue(
    val id: Long,
    val username: String,
    val type: String,
    val involvedInstitutions: ArrayList<InvolvedInstitution>,
    val title: String,
    val description: String,
    val location: String,
    val coordinates: String,
    val dateTime: String,
    val image: String,
    val status: String,
    val messages: ArrayList<Message>
):java.io.Serializable
