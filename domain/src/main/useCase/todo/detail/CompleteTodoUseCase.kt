package com.lab.clean.ktor.domain.useCase.todo.detail

import com.lab.clean.ktor.domain.core.Either
import com.lab.clean.ktor.domain.exception.Failure
import com.lab.clean.ktor.domain.repository.todo.detail.TodoDetailRepository
import com.lab.clean.ktor.domain.useCase.UseCase

class CompleteTodoUseCase constructor(
    private val userId: Int,
    private val repository: TodoDetailRepository
) : UseCase<Unit, Int>() {
    override fun run(params: Int): Either<Failure, Unit> {
        return repository.complete(userId, params)
    }
}