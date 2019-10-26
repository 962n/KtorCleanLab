package com.lab.clean.ktor.data.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction

object DBConnector {

    data class Config(
        val userName:String,
        val password:String,
        val maximumPoolSize:Int,
        val driverName:String,
        val jdbcUrl:String
    )

    private const val TRANSACTION_ISOLATION = "TRANSACTION_REPEATABLE_READ"

    private lateinit var innerMaster: Database
    private lateinit var innerSlave: Database

    val master: Database
        get() = innerMaster

    val slave: Database
        get() = innerSlave

    fun setUpSlave(config: Config) {
        val hikariConfig = toHikariConfig(config)
        hikariConfig.isReadOnly = true
        hikariConfig.isAutoCommit = false
        hikariConfig.validate()
        innerSlave = Database.connect(HikariDataSource(hikariConfig))
    }
    fun setUpMaster(config: Config) {
        val hikariConfig = toHikariConfig(config)
        hikariConfig.isAutoCommit = false
        hikariConfig.validate()
        innerMaster = Database.connect(HikariDataSource(hikariConfig))
    }

    private fun toHikariConfig(config:Config):HikariConfig {
        val hikariConfig = HikariConfig()
        hikariConfig.maximumPoolSize = config.maximumPoolSize
        hikariConfig.username = config.userName
        hikariConfig.password = config.password
        hikariConfig.transactionIsolation = TRANSACTION_ISOLATION
        hikariConfig.setDriverClassName(config.driverName)
        hikariConfig.jdbcUrl = config.jdbcUrl
        return hikariConfig
    }

}

fun <T> transactionMaster(statement: Transaction.() -> T): T {
    return transaction(DBConnector.master, statement)
}

fun <T> transactionSlave(statement: Transaction.() -> T): T {
    return transaction(DBConnector.slave, statement)
}