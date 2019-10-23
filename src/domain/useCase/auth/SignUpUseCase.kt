package com.lab.clean.ktor.domain.useCase.auth

import com.lab.clean.ktor.domain.core.Either
import com.lab.clean.ktor.domain.entity.auth.AuthEntity
import com.lab.clean.ktor.domain.exception.Failure
import com.lab.clean.ktor.domain.repository.auth.AuthRepository
import com.lab.clean.ktor.domain.useCase.UseCase

class SignUpUseCase constructor(private val repository: AuthRepository) : UseCase<AuthEntity, SignUpUseCase.Param>() {

    override fun run(params: Param): Either<Failure, AuthEntity> {
        return repository.signUp(params.toAuthRepositoryParam())
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