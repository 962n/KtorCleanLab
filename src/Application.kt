package com.lab.clean.ktor

import com.lab.clean.ktor.presentation.extension.respondApi
import com.lab.clean.ktor.presentation.ui.todo.*
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.http.HttpStatusCode
import io.ktor.locations.*
import io.ktor.routing.routing;

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalLocationsAPI
@Location("/sign_up")
data class SignUp(val email: String, val password: String)

@KtorExperimentalLocationsAPI
@Location("/sign_in")
data class SignIn(val email: String, val password: String)

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
@Location("/{id}")
data class TodoDetail(val id: Int) {
    @Location("")
    data class Update(val todoDetail: TodoDetail, val title: String)

    @Location("")
    data class Store(val todoDetail: TodoDetail, val title: String)
}


@KtorExperimentalLocationsAPI
@Location("/todo")
data class TodoIndex(
    val end_cursor: String?,
    val limit: Int?,
    val sort: Int?,
    val filter: Int?
)

@KtorExperimentalLocationsAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Locations)

    val client = HttpClient(Apache) {
    }

    routing {
        //TODO this routing is not working. analyze.
//        post<SignUp> { param ->
//            call.respondApi(SignUpController(param))
//        }
//        post<SignIn> { param ->
//            call.respondApi(SignInController(param))
//        }
//        get<TodoIndex> { param ->
//            call.respondApi(IndexTodoController(param))
//        }
//        post<TodoDetail.Store> { param ->
//            call.respondApi(StoreTodoController(param))
//        }
//        put<Todo.Detail.Update> { param ->
//            call.respondApi(UpdateTodoController(param))
//        }
        get<Todo.Detail> { param ->
            call.respondApi(ShowTodoController(param))
        }
        delete<Todo.Detail> { param ->
            call.respondApi(DelTodoController(param))
        }
    }
}

data class ApiResponse(val httpStatus: HttpStatusCode, val body: Any)

