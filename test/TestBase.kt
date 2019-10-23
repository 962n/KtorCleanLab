package com.lab.clean.ktor

import com.lab.clean.ktor.data.DBConnector
import com.lab.clean.ktor.data.TodosTable
import com.lab.clean.ktor.data.UsersTable
import com.zaxxer.hikari.HikariConfig
import io.ktor.util.KtorExperimentalAPI
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

open class TestBase {

    companion object {
        var isInit = false
    }
    private val testDbConfig:HikariConfig
        get() {
            val config = HikariConfig()
            config.maximumPoolSize = 3
            config.username = "docker"
            config.password = "docker"
            config.isAutoCommit = false
            config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            config.setDriverClassName("com.mysql.jdbc.Driver")
            config.jdbcUrl = "jdbc:mysql://localhost:3306/database"
            return config
        }

    @KtorExperimentalAPI
    @BeforeTest
    fun setUp() {
        if (!isInit) {
            println("database init")
            isInit = true
            DBConnector.connect(testDbConfig)
        }
    }

    @AfterTest
    fun tearDown() {
        transaction {
            UsersTable.deleteAll()
            TodosTable.deleteAll()
        }
    }
}
