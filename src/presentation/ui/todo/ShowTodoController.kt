package com.lab.clean.ktor.presentation.ui.todo

import com.lab.clean.ktor.ApiResponse
import com.lab.clean.ktor.Todo
import com.lab.clean.ktor.presentation.ui.BaseController
import io.ktor.locations.KtorExperimentalLocationsAPI

class ShowTodoController
@KtorExperimentalLocationsAPI
constructor(
    private val inputParam: Todo.Detail
) : BaseController() {
    override suspend fun execute(): ApiResponse {
        return ApiResponse("todo show in success")
    }
}