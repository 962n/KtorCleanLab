package com.lab.clean.ktor.data.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Payload
import java.util.Date

class JwtService(private val config: Config) {

    data class Config(
        val issuer: String,
        val realm: String,
        val secret: String,
        val audience: String,
        val validityInMin: Int
    )

    private val validityInMs = config.validityInMin * 60 * 1000
    private val algorithm = Algorithm.HMAC512(config.secret)
    val realm = config.realm
    val jwt = JWT.require(algorithm)
        .withIssuer(config.issuer)
        .build()
    val audience = config.audience

    companion object {
        private const val CLAIM_KEY_USER_ID = "user_id"
    }

    fun makeToken(userId: Int): String = JWT.create()
        .withSubject("Authentication")
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .withClaim(CLAIM_KEY_USER_ID, userId)
        .withExpiresAt(getExpiration())
        .sign(algorithm)

    fun getUserIdFromPayload(payload: Payload): Int {
        val userId = payload.getClaim(CLAIM_KEY_USER_ID).toString()
        return userId.toInt()
    }

    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)
}