package com.lab.clean.ktor.domain.useCase.auth

import com.lab.clean.ktor.domain.core.Either
import com.lab.clean.ktor.domain.entity.auth.AuthEntity
import com.lab.clean.ktor.domain.exception.Failure
import com.lab.clean.ktor.domain.repository.auth.AuthRepository
import com.lab.clean.ktor.domain.useCase.UseCase

class SignInUseCase constructor(private val repository: AuthRepository) : UseCase<AuthEntity, SignInUseCase.Param>() {

    override fun run(params: Param): Either<Failure, AuthEntity> {
        return repository.signIn(params.email, params.password)
    }

    data class Param(val email: String, val password: String)
}