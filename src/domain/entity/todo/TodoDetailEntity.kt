package com.lab.clean.ktor.domain.entity.todo

import java.util.*

data class TodoDetailEntity(
    var todoId: Int,
    val title: String?,
    val detail: String?,
    val deadLine: Date,
    val isComplete: Boolean
)