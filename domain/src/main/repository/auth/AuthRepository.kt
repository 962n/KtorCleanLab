package com.lab.clean.ktor.domain.repository.auth

import com.lab.clean.ktor.domain.core.Either
import com.lab.clean.ktor.domain.exception.Failure
import com.lab.clean.ktor.domain.exception.SignUpFailure

interface AuthRepository {
    data class SingUpParam(val name: String, val email: String, val password: String)
    fun signIn(email: String, password: String): Either<Failure, Int>
    fun signUp(param: SingUpParam): Either<SignUpFailure, Int>
}