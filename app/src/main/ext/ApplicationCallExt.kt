package com.lab.clean.ktor.app.ext

import com.lab.clean.ktor.app.AppPrincipal
import com.lab.clean.ktor.feature.coreComponent.ApiStatus
import com.lab.clean.ktor.feature.coreComponent.BaseController
import io.ktor.application.ApplicationCall
import io.ktor.auth.principal
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

suspend fun ApplicationCall.respondApi(controller: BaseController) {
    val response = controller.execute()
    this.respond(response.apiStatus.toHttpStatus(), response.body)
}

fun ApiStatus.toHttpStatus(): HttpStatusCode {
    return when (this) {
        ApiStatus.SUCCESS -> HttpStatusCode.OK
        ApiStatus.BAD_REQUEST -> HttpStatusCode.BadRequest
    }
}

fun ApplicationCall.appPrincipal(): AppPrincipal = principal()!!