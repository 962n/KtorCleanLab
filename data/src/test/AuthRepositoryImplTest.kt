package com.lab.clean.ktor.data

import com.lab.clean.ktor.data.database.table.UsersTable
import com.lab.clean.ktor.data.repositoryImpl.AuthRepositoryImpl
import com.lab.clean.ktor.domain.repository.auth.AuthRepository
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class AuthRepositoryImplTest : TestBase() {

    private val repository = AuthRepositoryImpl()

    @Test
    fun `sign up success`() {
        val name = "tarou"
        val email = "test@hoge.com"
        val password = "fugfuga"

        val result = transaction {
            repository.signUp(AuthRepository.SingUpParam(name, email, password))
        }
        assertTrue(result.isRight)
        transaction {

            val user = UsersTable.select {
                UsersTable.email.eq(email) and UsersTable.name.eq(name)
            }.firstOrNull()
            if (user == null) {
                fail("do not expect user record null!!")
                return@transaction
            }
            val actualHashPass: String = user[UsersTable.password]
            val expectHashPass: String = repository.makeHashPassword(password, user[UsersTable.salt])
            assertEquals(expectHashPass, actualHashPass)
        }
    }

    @Test
    fun `sign up already exist failure`() {
        val name = "tarou"
        val email = "test@hoge.com"

        transaction {
            UsersTable.insert {
                it[this.name] = name
                it[this.email] = email
                it[this.password] = "password"
                it[this.salt] = "salt"
            }
        }
        val password = "fugfuga"
        val result = transaction {
            repository.signUp(AuthRepository.SingUpParam(name, email, password))
        }
        assertTrue(result.isLeft)
    }

    @Test
    fun `sign in success`() {
        val name = "tarou"
        val email = "test@hoge.com"
        val password = "fugfuga"
        val salt = "salt"
        val passwordHash = repository.makeHashPassword(password, salt)
        val expectUserId = transaction {
            UsersTable.insert {
                it[this.name] = name
                it[this.email] = email
                it[this.password] = passwordHash
                it[this.salt] = salt
            } get UsersTable.id
        }
        val result = transaction {
            repository.signIn(email, password)
        }
        result.either(
            {
                fail("do not expect this route!!")
            },
            { userId ->
                assertEquals(expectUserId, userId)
            }
        )
    }

    @Test
    fun `sign in not exist failure`() {
        val email = "test@hoge.com"
        val password = "fugfuga"

        val result = transaction {
            repository.signIn(email, password)
        }
        assertTrue(result.isLeft)
    }

    @Test
    fun `sign in password wrong failure`() {
        val name = "tarou"
        val email = "test@hoge.com"
        val password = "fugfuga"
        val salt = "salt"
        val passwordHash = repository.makeHashPassword(password, salt)
        transaction {
            UsersTable.insert {
                it[this.name] = name
                it[this.email] = email
                it[this.password] = passwordHash
                it[this.salt] = salt
            }
        }
        val result = transaction {
            repository.signIn(email, "hogehge")
        }
        assertTrue(result.isLeft)
    }
}
