package com.lab.clean.ktor.app.routing

import com.lab.clean.ktor.app.ext.appPrincipal
import com.lab.clean.ktor.app.ext.respondApi
import com.lab.clean.ktor.feature.todo.DelTodoController
import com.lab.clean.ktor.feature.todo.IndexTodoController
import com.lab.clean.ktor.feature.todo.ShowTodoController
import com.lab.clean.ktor.feature.todo.StoreTodoController
import com.lab.clean.ktor.feature.todo.UpdateTodoController
import io.ktor.application.call
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.delete
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.locations.put
import io.ktor.routing.Route
import io.ktor.util.KtorExperimentalAPI

@KtorExperimentalLocationsAPI
@Location("/todo")
class Todo {
    @Location("")
    data class Index(
        val end_cursor: String?,
        val limit: Int?,
        val sort: Int?,
        val filter: Int?
    )

    @Location("/{id}")
    data class Detail(val id: Int) {
        @Location("")
        data class Update(val title: String)

        @Location("")
        data class Store(val title: String)
    }
}

@KtorExperimentalLocationsAPI
@Location("/todo/{id}")
data class TodoDetail(val id: Int) {
    @Location("")
    data class Update(val todoDetail: TodoDetail, val title: String = "")

    @Location("")
    data class Store(val todoDetail: TodoDetail, val title: String = "")
}

@KtorExperimentalLocationsAPI
@Location("/todo")
class TodoIndex(
    val end_cursor: String? = null,
    val limit: Int? = null,
    val sort: Int? = null,
    val filter: Int? = null
)

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun Route.routingTodo() {
    get<TodoIndex> { param ->
        call.respondApi(IndexTodoController(IndexTodoController.InputParam()))
    }

    post<TodoDetail.Store> { param ->
        call.respondApi(StoreTodoController(call.appPrincipal().userId, StoreTodoController.InputParam(null)))
    }
    put<TodoDetail.Update> { param ->
        call.respondApi(UpdateTodoController(call.appPrincipal().userId, UpdateTodoController.InputParam(null)))
    }
    get<Todo.Detail> { param ->
        call.respondApi(ShowTodoController(ShowTodoController.InputParam(1)))
    }
    delete<Todo.Detail> { param ->
        call.respondApi(DelTodoController(DelTodoController.InputParam(1)))
    }
}