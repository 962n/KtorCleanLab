package com.lab.clean.ktor.domain.useCase

import com.lab.clean.ktor.domain.core.Either
import com.lab.clean.ktor.domain.exception.Failure

abstract class UseCase<out Type, in Params> where Type : Any {

    abstract fun run(params: Params): Either<Failure, Type>

    operator fun invoke(params: Params): Either<Failure, Type> {
        return run(params)
    }
    object None
}