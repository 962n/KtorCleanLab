package com.lab.clean.ktor.data

import com.lab.clean.ktor.domain.AtomicProcessor
import com.lab.clean.ktor.domain.core.Either
import com.lab.clean.ktor.domain.exception.Failure
import io.ktor.util.KtorExperimentalAPI

object AtomicProcessorImpl : AtomicProcessor{
    @KtorExperimentalAPI
    override fun <L : Failure, R> readWrite(statement: () -> Either<L, R>): Either<L, R> {
        return transactionMaster {
            val result = statement()
            result.either({
                rollback()
            },{
                commit()
            })
            result
        }
    }

    @KtorExperimentalAPI
    override fun <T> readOnly(statement: () -> T): T {
        return transactionSlave {
            statement()
        }
    }
}