package com.lab.clean.ktor.presentation.ui.todo

import com.lab.clean.ktor.ApiResponse
import com.lab.clean.ktor.Todo
import com.lab.clean.ktor.TodoDetail
import com.lab.clean.ktor.presentation.ui.BaseController
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI

class StoreTodoController
@KtorExperimentalLocationsAPI
constructor(
    private val inputParam: TodoDetail.Store
) : BaseController() {
    override fun execute(): ApiResponse {
        return ApiResponse("todo store in success")
    }
}