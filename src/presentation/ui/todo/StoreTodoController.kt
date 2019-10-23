package com.lab.clean.ktor.presentation.ui.todo

import com.lab.clean.ktor.ApiResponse
import com.lab.clean.ktor.TodoDetail
import com.lab.clean.ktor.presentation.AppPrincipal
import com.lab.clean.ktor.presentation.ui.BaseController
import io.ktor.locations.KtorExperimentalLocationsAPI

class StoreTodoController
@KtorExperimentalLocationsAPI
constructor(
    private val inputParam: TodoDetail.Store,
    private val principal: AppPrincipal
) : BaseController() {
    override suspend fun execute(): ApiResponse {
        return ApiResponse("todo store in success")
    }
}