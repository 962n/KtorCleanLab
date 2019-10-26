package com.lab.clean.ktor.data.repositoryImpl

import com.lab.clean.ktor.data.database.table.UsersTable
import com.lab.clean.ktor.domain.core.Either
import com.lab.clean.ktor.domain.exception.Failure
import com.lab.clean.ktor.domain.exception.SignUpFailure
import com.lab.clean.ktor.domain.exception.SignUpFailureType
import com.lab.clean.ktor.domain.repository.auth.AuthRepository
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.security.MessageDigest

class AuthRepositoryImpl : AuthRepository {

    override fun signIn(email: String, password: String): Either<Failure, Int> {
        val user = UsersTable.slice(
            UsersTable.id,
            UsersTable.password,
            UsersTable.salt
        ).select {
            UsersTable.email eq email
        }.firstOrNull() ?: return Either.Left(SignUpFailure(SignUpFailureType.ALREADY_EXIST))

        val hashPassword = makeHashPassword(password, user[UsersTable.salt])
        if (hashPassword != user[UsersTable.password]) {
            return Either.Left(SignUpFailure(SignUpFailureType.ALREADY_EXIST))
        }
        return Either.Right(user[UsersTable.id])
    }

    override fun signUp(param: AuthRepository.SingUpParam): Either<SignUpFailure, Int> {
        val exists = UsersTable
            .select {
                UsersTable.email eq param.email
            }
            .count() > 0

        if (exists) {
            return Either.Left(SignUpFailure(SignUpFailureType.ALREADY_EXIST))
        }
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val salt = (1..30)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")

        val hashPassword = makeHashPassword(param.password, salt)

        val userId = UsersTable.insert {
            it[this.name] = param.name
            it[this.email] = param.email
            it[this.password] = hashPassword
            it[this.salt] = salt
        } get UsersTable.id

        return Either.Right(userId)
    }

    fun makeHashPassword(password: String, salt: String): String {
        val concatPass = password + salt
        return MessageDigest
            .getInstance("SHA-256")
            .digest(concatPass.toByteArray())
            .joinToString("") {
                "%02x".format(it)
            }
    }
}