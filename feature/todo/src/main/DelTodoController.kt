package com.lab.clean.ktor.feature.todo

import com.lab.clean.ktor.feature.coreComponent.ApiResponse
import com.lab.clean.ktor.feature.coreComponent.BaseController

class DelTodoController
constructor(
    private val param: InputParam
) : BaseController() {
    data class InputParam(val todoId:Int)
    override suspend fun execute(): ApiResponse {
        return ApiResponse("todo del in success")
    }
}