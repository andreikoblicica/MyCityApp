package com.example.communityappmobile.models.auth

data class AuthResponse(
    var token: String,
    var id: Long,
    var username: String,
    var role: String,
    var name: String,
    var email: String
)
