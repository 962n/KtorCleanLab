package com.lab.clean.ktor.app.data

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Payload
import com.lab.clean.ktor.app.AppPrincipal
import io.ktor.application.ApplicationEnvironment
import io.ktor.util.KtorExperimentalAPI
import java.util.*

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
    private val validityInMs = environment.config.property("jwt.validity_in_min").getString().toInt() * 60 * 1000

    companion object {
        private const val CLAIM_KEY_USER_ID = "user_id"
    }

    /**
     * Produce a token for this combination of User and Account
     */
    fun makeToken(userId: Int): String = JWT.create()
        .withSubject("Authentication")
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim(CLAIM_KEY_USER_ID, userId)
        .withExpiresAt(getExpiration())
        .sign(algorithm)

    fun principal(payload: Payload): AppPrincipal {
        val userId = payload.getClaim(CLAIM_KEY_USER_ID).toString()
        return AppPrincipal(userId.toInt())
    }

    /**
     * Calculate the expiration Date based on current time + the given validity
     */
    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)
}