package com.lab.clean.ktor.app

import com.lab.clean.ktor.app.ext.jwtConfig
import com.lab.clean.ktor.app.ext.masterDBConfig
import com.lab.clean.ktor.app.ext.slaveDBConfig
import com.lab.clean.ktor.app.routing.routingAuth
import com.lab.clean.ktor.data.database.DBConnector
import com.lab.clean.ktor.app.routing.routingTodo
import com.lab.clean.ktor.data.jwt.JwtService
import io.ktor.application.*
import io.ktor.auth.Principal
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.auth.jwt.jwt
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.locations.*
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

data class AppPrincipal constructor(val userId: Int) : Principal

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    DBConnector.setUpSlave(environment.slaveDBConfig())
    DBConnector.setUpMaster(environment.masterDBConfig())

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    install(Locations)

    val jwtService = JwtService(environment.jwtConfig())
    authentication {
        jwt {
            this.realm = jwtService.realm
            verifier(jwtService.jwt)
            validate { credential ->
                if (credential.payload.audience.contains(jwtService.audience)) {
                    val userId = jwtService.getUserIdFromPayload(credential.payload)
                    AppPrincipal(userId)
                } else {
                    throw IllegalStateException("unauthorized")
                }
            }
        }
    }

    val client = HttpClient(Apache) {
    }

    routing {
        routingAuth(jwtService)
        authenticate {
            routingTodo()
        }
    }
}
