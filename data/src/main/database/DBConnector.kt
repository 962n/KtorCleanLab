package com.lab.clean.ktor.data.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.util.KtorExperimentalAPI
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction

@KtorExperimentalAPI
object DBConnector {

    private lateinit var innerMaster: Database
    private lateinit var innerSlave: Database

    val master: Database
        get() = innerMaster

    val slave: Database
        get() = innerSlave

    fun setUpSlave(config: HikariConfig) {
        config.validate()
        innerSlave = Database.connect(HikariDataSource(config))
    }

    fun setUpMaster(config: HikariConfig) {
        config.validate()
        innerMaster = Database.connect(HikariDataSource(config))
    }

}

@KtorExperimentalAPI
fun <T> transactionMaster(statement: Transaction.() -> T): T {
    return transaction(DBConnector.master, statement)
}

@KtorExperimentalAPI
fun <T> transactionSlave(statement: Transaction.() -> T): T {
    return transaction(DBConnector.slave, statement)
}