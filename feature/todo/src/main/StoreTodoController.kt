package com.lab.clean.ktor.feature.todo

import com.lab.clean.ktor.feature.coreComponent.ApiResponse
import com.lab.clean.ktor.feature.coreComponent.BaseController

class StoreTodoController
constructor(
    private val userId: Int,
    private val param: InputParam
) : BaseController() {
    data class InputParam(val title: String?)

    override suspend fun execute(): ApiResponse {
        return ApiResponse("todo store in success")
    }
}