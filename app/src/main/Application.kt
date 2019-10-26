package com.lab.clean.ktor.app

import com.lab.clean.ktor.app.config.DBConfig
import com.lab.clean.ktor.app.data.JwtConfig
import com.lab.clean.ktor.app.routing.routingAuth
import com.lab.clean.ktor.data.database.DBConnector
import com.lab.clean.ktor.app.routing.routingTodo
import io.ktor.application.*
import io.ktor.auth.Authentication
import io.ktor.auth.Principal
import io.ktor.auth.authenticate
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

    val dbConfig = DBConfig(environment)
    DBConnector.setUpSlave(dbConfig.slave)
    DBConnector.setUpMaster(dbConfig.master)

    val client = HttpClient(Apache) {
    }

    routing {
        routingAuth(jwtConfig)
        authenticate {
            routingTodo()
        }
    }
}
