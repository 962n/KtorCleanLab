package com.lab.clean.ktor

import com.lab.clean.ktor.data.DBConnector
import com.lab.clean.ktor.data.JwtConfig
import com.lab.clean.ktor.presentation.extension.appPrincipal
import com.lab.clean.ktor.presentation.extension.respondApi
import com.lab.clean.ktor.presentation.response.AuthResponse
import com.lab.clean.ktor.presentation.ui.signIn.SignInController
import com.lab.clean.ktor.presentation.ui.signUp.SignUpController
import com.lab.clean.ktor.presentation.ui.todo.*
import io.ktor.application.*
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.jwt
import io.ktor.auth.principal
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.locations.*
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@KtorExperimentalLocationsAPI
@Location("/sign_up")
data class SignUp(val name: String? = null, val email: String? = null, val password: String? = null)

@KtorExperimentalLocationsAPI
suspend fun ApplicationCall.getSignUpParam(): SignUp {
    val parameters = this.receiveParameters()
    return SignUp(
        parameters.get("name"),
        parameters.get("email"),
        parameters.get("password")
    )
}

@KtorExperimentalLocationsAPI
suspend fun ApplicationCall.getSignInParam(): SignIn {
    val parameters = this.receiveParameters()
    return SignIn(
        parameters.get("email"),
        parameters.get("password")
    )
}

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
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    install(Locations)

    val jwtConfig = JwtConfig(environment)
    install(Authentication) {
        jwt {
            this.realm = jwtConfig.realm
            verifier(jwtConfig.jwt)
            validate { credential ->
                if (credential.payload.audience.contains(jwtConfig.audience)) {
                    jwtConfig.principal(credential.payload)
                } else {
                    throw IllegalStateException("unauthorized")
                }
            }
        }
    }

    DBConnector.connect(DBConnector.toConfig(environment))

    val client = HttpClient(Apache) {
    }

    routing {
        post<SignUp> { _ ->
            call.respondApi(SignUpController(call.getSignUpParam(), jwtConfig))
        }
        post<SignIn> { param ->
            call.respondApi(SignInController(call.getSignInParam(), jwtConfig))
        }

        authenticate {
            route("/hoge") {
                get {
                    call.respond(AuthResponse(1, "hoge"))
                }
            }
            get<TodoIndex> { param ->
                call.respondApi(IndexTodoController(param))
            }
            post<TodoDetail.Store> { param ->
                call.respondApi(StoreTodoController(param, call.appPrincipal()))
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
