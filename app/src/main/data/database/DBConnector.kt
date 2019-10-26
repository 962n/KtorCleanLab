package com.lab.clean.ktor.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.ApplicationEnvironment
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


    private fun toSlaveConfig(environment: ApplicationEnvironment): HikariConfig {
        val config = HikariConfig()

        config.maximumPoolSize = environment
            .config
            .property("database.slave.pool_size")
            .getString()
            .toInt()

        config.username = environment
            .config
            .property("database.slave.username")
            .getString()

        config.password = environment
            .config
            .property("database.slave.password")
            .getString()

        config.isReadOnly = true
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"

        config.setDriverClassName(
            environment
                .config
                .property("database.slave.driver_name")
                .getString()
        )

        config.jdbcUrl = environment
            .config
            .property("database.slave.jdbc_url")
            .getString()
        return config
    }

    private fun toMasterConfig(environment: ApplicationEnvironment): HikariConfig {
        val config = HikariConfig()

        config.maximumPoolSize = environment
            .config
            .property("database.master.pool_size")
            .getString()
            .toInt()

        config.username = environment
            .config
            .property("database.master.username")
            .getString()

        config.password = environment
            .config
            .property("database.master.password")
            .getString()

        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"

        config.setDriverClassName(
            environment
                .config
                .property("database.master.driver_name")
                .getString()
        )

        config.jdbcUrl = environment
            .config
            .property("database.master.jdbc_url")
            .getString()
        return config
    }

    fun setUp(environment: ApplicationEnvironment) {

        val masterConfig = toMasterConfig(environment)
        val slaveConfig = toSlaveConfig(environment)

        masterConfig.validate()
        innerMaster = Database.connect(HikariDataSource(masterConfig))
        slaveConfig.validate()
        innerSlave = Database.connect(HikariDataSource(slaveConfig))
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