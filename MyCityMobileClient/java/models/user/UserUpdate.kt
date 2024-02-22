package com.example.communityappmobile.models.user

data class UserUpdate(
    val id: Long,
    var name: String,
    val username: String,
    val email: String,
    var password: String
)
