package com.example.communityappmobile.models.issue

data class InvolvedInstitution(
    val id:Long,
    val institutionName: String,
    val username: String?,
    val issueStatus: String
):java.io.Serializable
