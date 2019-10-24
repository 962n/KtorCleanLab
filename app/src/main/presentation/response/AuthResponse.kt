package com.lab.clean.ktor.presentation.response

import com.google.gson.annotations.SerializedName

data class AuthResponse constructor(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("token")
    val token: String
)