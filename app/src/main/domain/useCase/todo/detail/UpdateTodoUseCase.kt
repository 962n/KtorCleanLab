package com.lab.clean.ktor.domain.useCase.todo.detail

import com.lab.clean.ktor.domain.core.Either
import com.lab.clean.ktor.domain.exception.Failure
import com.lab.clean.ktor.domain.repository.todo.detail.TodoDetailRepository

class UpdateTodoUseCase constructor(
    private val userId: Int,
    private val todoId: Int,
    private val repository: TodoDetailRepository
) : AbsWriteTodoUseCase() {
    override fun run(params: Params): Either<Failure, Unit> {
        val writeParam = TodoDetailRepository.WriteParam(
            params.title,
            params.detail,
            params.deadLine
        )
        return repository.update(userId, todoId, writeParam)
    }
}