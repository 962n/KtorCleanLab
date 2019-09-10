package com.lab.clean.ktor.domain.useCase.todo.detail

import com.lab.clean.ktor.domain.useCase.UseCase
import java.util.*

abstract class AbsWriteTodoUseCase : UseCase<Unit, AbsWriteTodoUseCase.Params>() {

    data class Params(
        val title: String?,
        val detail: String?,
        val deadLine: Date
    )
}