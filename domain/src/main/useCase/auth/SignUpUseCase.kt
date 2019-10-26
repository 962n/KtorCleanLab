package com.lab.clean.ktor.domain.useCase.auth

import com.lab.clean.ktor.domain.core.Either
import com.lab.clean.ktor.domain.core.rightMap
import com.lab.clean.ktor.domain.entity.auth.AuthEntity
import com.lab.clean.ktor.domain.exception.Failure
import com.lab.clean.ktor.domain.repository.auth.AuthRepository
import com.lab.clean.ktor.domain.repository.auth.TokenGenerator
import com.lab.clean.ktor.domain.useCase.UseCase

class SignUpUseCase
constructor(
    private val repository: AuthRepository,
    private val tokenGenerator: TokenGenerator
) : UseCase<AuthEntity, SignUpUseCase.Param>() {

    override fun run(params: Param): Either<Failure, AuthEntity> {
        val result = repository.signUp(params.toAuthRepositoryParam())
        return result.rightMap {
            val token = tokenGenerator.generate(it)
            AuthEntity(it,token)
        }
    }

    data class Param(val name: String, val email: String, val password: String)
}

fun SignUpUseCase.Param.toAuthRepositoryParam(): AuthRepository.SingUpParam {
    return AuthRepository.SingUpParam(
        name,
        email,
        password
    )
}