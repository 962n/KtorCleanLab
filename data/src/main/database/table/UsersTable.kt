package com.lab.clean.ktor.data.database.table

import org.jetbrains.exposed.sql.Table

object UsersTable : Table("users") {
    val id = integer("id").primaryKey().autoIncrement()
    val name = varchar("name", 20)
    val email = varchar("email", 100)
    var password = varchar("password", 64)
    var salt = varchar("salt", 30)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}
