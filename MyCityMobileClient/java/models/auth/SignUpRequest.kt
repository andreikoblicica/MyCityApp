package com.example.communityappmobile.models.auth

data class SignUpRequest(
    var name: String,
    val username: String,
    val email: String,
    var password: String,
    val role: String="Regular User"
)
