package com.lab.clean.ktor.feature.coreComponent

data class ApiResponse(val apiStatus: ApiStatus, val body: Any) {
    constructor(body: Any) : this(ApiStatus.SUCCESS, body)
}
