package com.lab.clean.ktor.presentation.ui.todo

import com.lab.clean.ktor.ApiResponse
import com.lab.clean.ktor.Todo
import com.lab.clean.ktor.presentation.ui.BaseController
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI

class ShowTodoController
@KtorExperimentalLocationsAPI
constructor(
    private val inputParam: Todo.Detail
) : BaseController() {
    override fun execute(): ApiResponse {
        return ApiResponse(HttpStatusCode.OK, "todo show in success")
    }
}