package com.lab.clean.ktor.domain.repository.todo

import com.lab.clean.ktor.domain.entity.todo.TodoListEntity

interface TodoListRepository {
    fun fetch(endCursor: String?, limit: Int): List<TodoListEntity>
}