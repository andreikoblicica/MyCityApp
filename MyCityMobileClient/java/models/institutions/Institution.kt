package com.example.communityappmobile.models.institutions

data class Institution(
    val id: Long,
    val name: String,
    val address: String,
    val email:String,
    val phoneNumber: String,
    val website: String,
    val image: String
):java.io.Serializable
