package com.lab.clean.ktor.presentation.ui.todo

import com.lab.clean.ktor.ApiResponse
import com.lab.clean.ktor.TodoIndex
import com.lab.clean.ktor.domain.useCase.todo.list.GetTodoListUseCase
import com.lab.clean.ktor.presentation.ui.BaseController
import com.lab.clean.ktor.presentation.extension.apiResponse
import io.ktor.locations.KtorExperimentalLocationsAPI

class IndexTodoController
@KtorExperimentalLocationsAPI
constructor(
    private val inputParam: TodoIndex
) : BaseController() {

    lateinit var useCase: GetTodoListUseCase

    override suspend fun execute(): ApiResponse {
        if (true) {
            return ApiResponse("todo index in success")
        }
        val result = useCase(1)
        return result.apiResponse({
            ApiResponse(Unit)
        }, {
            ApiResponse(Unit)
        })
    }
}