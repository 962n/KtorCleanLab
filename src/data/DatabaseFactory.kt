package com.lab.clean.ktor.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

object DatabaseFactory {
    fun initialize() {
        val config = HikariConfig()
        config.maximumPoolSize = 3
        config.setDriverClassName("com.mysql.jdbc.Driver")
        config.jdbcUrl = "jdbc:mysql://localhost:3306/database"
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.username = "docker"
        config.password = "docker"
        config.validate()
        Database.connect(HikariDataSource(config))
    }
}