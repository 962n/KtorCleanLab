package com.lab.clean.ktor

import com.lab.clean.ktor.data.DBConnector
import com.lab.clean.ktor.data.JwtConfig
import com.lab.clean.ktor.presentation.extension.respondApi
import com.lab.clean.ktor.presentation.ui.signIn.SignInController
import com.lab.clean.ktor.presentation.ui.signUp.SignUpController
import com.lab.clean.ktor.presentation.ui.todo.*
import io.ktor.application.*
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.http.HttpStatusCode
import io.ktor.locations.*
import io.ktor.routing.routing;
import io.ktor.util.KtorExperimentalAPI

fun main(args: Array<String>): Unit {
    io.ktor.server.netty.EngineMain.main(args)
}

@KtorExperimentalLocationsAPI
@Location("/sign_up")
data class SignUp(val email: String? = null, val password: String? = null)

@KtorExperimentalLocationsAPI
@Location("/sign_in")
data class SignIn(val email: String? = null, val password: String? = null)

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
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Locations)

    val jwtConfig = JwtConfig(environment)
    install(Authentication) {
        jwt {
            this.realm = jwtConfig.realm
            verifier(jwtConfig.jwt)
            validate { credential ->
                if (credential.payload.audience.contains(jwtConfig.audience)) {
                    JWTPrincipal(credential.payload)
                } else {
                    throw IllegalStateException("unauthorized")
                }
            }
        }
    }

    DBConnector.connect(environment)

    val client = HttpClient(Apache) {
    }

    routing {
        post<SignUp> { param ->
            call.respondApi(SignUpController(param))
        }
        post<SignIn> { param ->
            call.respondApi(SignInController(param))
        }

        authenticate {
            get<TodoIndex> { param ->
                call.respondApi(IndexTodoController(param))
            }
            post<TodoDetail.Store> { param ->
                call.respondApi(StoreTodoController(param))
            }
            put<TodoDetail.Update> { param ->
                call.respondApi(UpdateTodoController(param))
            }
            get<Todo.Detail> { param ->
                call.respondApi(ShowTodoController(param))
            }
            delete<Todo.Detail> { param ->
                call.respondApi(DelTodoController(param))
            }
        }
    }
}

data class ApiResponse(val httpStatus: HttpStatusCode, val body: Any) {
    constructor(body: Any) : this(HttpStatusCode.OK, body)
}

