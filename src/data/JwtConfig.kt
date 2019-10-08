package com.lab.clean.ktor.data

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.ApplicationEnvironment
import io.ktor.util.KtorExperimentalAPI

@KtorExperimentalAPI
class JwtConfig(environment: ApplicationEnvironment) {
    private val issuer = environment.config.property("jwt.issuer").getString()
    val realm = environment.config.property("jwt.realm").getString()
    private val secret = environment.config.property("jwt.secret").getString()
    private val algorithm = Algorithm.HMAC512(secret)
    val audience = environment.config.property("jwt.audience").getString()
    val jwt = JWT.require(algorithm)
        .withIssuer(issuer)
        .build()

}