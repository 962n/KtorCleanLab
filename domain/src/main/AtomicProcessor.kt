package com.lab.clean.ktor.domain

import com.lab.clean.ktor.domain.core.Either
import com.lab.clean.ktor.domain.exception.Failure

interface AtomicProcessor {
    fun <L : Failure, R> readWrite(statement: () -> Either<L, R>): Either<L, R>
    fun <T> readOnly(statement: () -> T): T
}