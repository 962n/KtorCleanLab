package com.lab.clean.ktor.domain.repository.auth

import com.lab.clean.ktor.domain.core.Either
import com.lab.clean.ktor.domain.entity.auth.AuthEntity
import com.lab.clean.ktor.domain.exception.Failure

interface AuthRepository {
    fun signIn(email: String, password: String): Either<Failure, AuthEntity>
    fun signUp(email: String, password: String): Either<Failure, AuthEntity>
}