package com.lab.clean.ktor.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.ApplicationEnvironment
import io.ktor.util.KtorExperimentalAPI
import org.jetbrains.exposed.sql.Database

@KtorExperimentalAPI
object DBConnector {

    fun toConfig(environment: ApplicationEnvironment) : HikariConfig {
        val config = HikariConfig()

        config.maximumPoolSize = environment
            .config
            .property("database.pool_size")
            .getString()
            .toInt()

        config.username = environment
            .config
            .property("database.username")
            .getString()

        config.password = environment
            .config
            .property("database.password")
            .getString()

        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"

        config.setDriverClassName(environment
            .config
            .property("database.driver_name")
            .getString()
        )

        config.jdbcUrl = environment
            .config
            .property("database.jdbc_url")
            .getString()
        return config
    }

    fun connect(config: HikariConfig) {
        config.validate()
        Database.connect(HikariDataSource(config))
    }
}