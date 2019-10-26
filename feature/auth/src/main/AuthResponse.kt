package com.lab.clean.ktor.feature.auth

import com.google.gson.annotations.SerializedName

data class AuthResponse constructor(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("token")
    val token: String
)