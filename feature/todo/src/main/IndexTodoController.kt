package com.lab.clean.ktor.feature.todo

import com.lab.clean.ktor.domain.useCase.todo.list.GetTodoListUseCase
import com.lab.clean.ktor.feature.coreComponent.ApiResponse
import com.lab.clean.ktor.feature.coreComponent.BaseController
import com.lab.clean.ktor.feature.coreComponent.apiResponse

class IndexTodoController
constructor(
    private val param: InputParam
) : BaseController() {
    data class InputParam(
        val end_cursor: String? = null,
        val limit: Int? = null,
        val sort: Int? = null,
        val filter: Int? = null
    )

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