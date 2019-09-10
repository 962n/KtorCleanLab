package com.lab.clean.ktor.domain.repository.todo

import com.lab.clean.ktor.domain.core.Either
import com.lab.clean.ktor.domain.entity.todo.TodoDetailEntity
import com.lab.clean.ktor.domain.exception.Failure
import java.util.*

interface TodoDetailRepository {

    data class WriteParam(
        val title: String?,
        val detail: String?,
        val deadLine: Date
    )

    fun fetch(userId: Int, todoId: Int): Either<Failure, TodoDetailEntity>
    fun store(userId: Int, todoId: Int, param: WriteParam): Either<Failure, Unit>
    fun delete(userId: Int, todoId: Int): Either<Failure, Unit>
    fun update(userId: Int, todoId: Int, param: WriteParam): Either<Failure, Unit>
    fun complete(userId: Int, todoId: Int): Either<Failure, Unit>

}