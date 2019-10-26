package com.lab.clean.ktor.data

import com.lab.clean.ktor.data.database.table.TodosTable
import com.lab.clean.ktor.data.database.table.UsersTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before

open class TestBase {

    companion object {
        var isInit = false
    }
    private val testDbConfig: HikariConfig
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


    @Before
    fun setUp() {
        if (!isInit) {
            isInit = true
            testDbConfig.validate()
            Database.connect(HikariDataSource(testDbConfig))
        }
    }

    @After
    fun tearDown() {
        transaction {
            UsersTable.deleteAll()
            TodosTable.deleteAll()
        }
    }
}
