package com.lab.clean.ktor.data

import com.lab.clean.ktor.domain.repository.auth.TokenGenerator
import io.ktor.util.KtorExperimentalAPI

class TokenGeneratorImpl
@KtorExperimentalAPI
constructor(
    private val jwtConfig: JwtConfig
) : TokenGenerator {

    @KtorExperimentalAPI
    override fun generate(userId: Int): String {
        return jwtConfig.makeToken(userId)
    }
}