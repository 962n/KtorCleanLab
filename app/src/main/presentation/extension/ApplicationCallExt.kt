package com.lab.clean.ktor.presentation.extension

import com.lab.clean.ktor.presentation.AppPrincipal
import com.lab.clean.ktor.presentation.ui.BaseController
import io.ktor.application.ApplicationCall
import io.ktor.auth.principal
import io.ktor.response.respond

suspend fun ApplicationCall.respondApi(controller: BaseController) {
    val response = controller.execute()
    this.respond(response.httpStatus, response.body)
}
fun ApplicationCall.appPrincipal(): AppPrincipal = principal()!!