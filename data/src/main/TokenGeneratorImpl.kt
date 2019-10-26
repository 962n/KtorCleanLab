package com.lab.clean.ktor.data

import com.lab.clean.ktor.data.jwt.JwtService
import com.lab.clean.ktor.domain.repository.auth.TokenGenerator

class TokenGeneratorImpl
constructor(
    private val jwtService: JwtService
) : TokenGenerator {

    override fun generate(userId: Int): String {
        return jwtService.makeToken(userId)
    }
}