package com.lab.clean.ktor.app.config

import com.zaxxer.hikari.HikariConfig
import io.ktor.application.ApplicationEnvironment
import io.ktor.util.KtorExperimentalAPI

@KtorExperimentalAPI
class DBConfig constructor(private val environment: ApplicationEnvironment) {
    val slave: HikariConfig
        get() = run {
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
            config
        }

    val master: HikariConfig
        get() = run {
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
            config
        }
}