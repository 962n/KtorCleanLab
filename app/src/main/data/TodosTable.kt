package com.lab.clean.ktor.data

import org.jetbrains.exposed.sql.Table

object TodosTable : Table("todos") {
    val id = integer("id").primaryKey().autoIncrement()
    val userId = integer("user_id").references(UsersTable.id)
    val title = varchar("title", 100).nullable()
    val content = text("content", "utf8_bin")
    val deadLineAt = datetime("dead_line_at")
    val completedAt = datetime("completed_at").nullable()
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}
