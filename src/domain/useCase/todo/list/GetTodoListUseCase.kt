package com.lab.clean.ktor.domain.useCase.todo.list

import com.lab.clean.ktor.domain.core.Either
import com.lab.clean.ktor.domain.entity.todo.TodoListEntity
import com.lab.clean.ktor.domain.exception.Failure
import com.lab.clean.ktor.domain.useCase.UseCase

class GetTodoListUseCase : UseCase<List<TodoListEntity>,Int>(){
    override fun run(params: Int): Either<Failure, List<TodoListEntity>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}