package com.lab.clean.ktor.data

import com.lab.clean.ktor.data.database.transactionMaster
import com.lab.clean.ktor.data.database.transactionSlave
import com.lab.clean.ktor.domain.Transactor
import com.lab.clean.ktor.domain.core.Either
import com.lab.clean.ktor.domain.exception.Failure
import io.ktor.util.KtorExperimentalAPI

object TransactorImpl : Transactor{
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