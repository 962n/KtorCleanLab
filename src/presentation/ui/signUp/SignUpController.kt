package com.lab.clean.ktor.presentation.ui.signUp

import com.lab.clean.ktor.ApiResponse
import com.lab.clean.ktor.SignUp
import com.lab.clean.ktor.data.JwtConfig
import com.lab.clean.ktor.data.repositoryImpl.AuthRepositoryImpl
import com.lab.clean.ktor.data.transactionMaster
import com.lab.clean.ktor.data.transactionSlave
import com.lab.clean.ktor.domain.useCase.auth.SignUpUseCase
import com.lab.clean.ktor.presentation.ui.BaseController
import com.lab.clean.ktor.presentation.extension.apiResponse
import com.lab.clean.ktor.presentation.extension.isEmail
import com.lab.clean.ktor.presentation.response.AuthResponse
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.util.KtorExperimentalAPI
import org.h2.mvstore.MVStoreTool.rollback
import org.jetbrains.exposed.sql.transactions.experimental.transaction

class SignUpController
@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
constructor(
    private val param: SignUp,
    private val jwtConfig: JwtConfig
) : BaseController() {

    lateinit var useCase: SignUpUseCase

    @KtorExperimentalAPI
    @KtorExperimentalLocationsAPI
    override suspend fun execute(): ApiResponse {
        inject()
        val email = param.email ?: return ApiResponse(HttpStatusCode.BadRequest, Unit)
        val password = param.password ?: return ApiResponse(HttpStatusCode.BadRequest, Unit)
        val name = param.name ?: return ApiResponse(HttpStatusCode.BadRequest, Unit)
        if (!email.isEmail()) {
            ApiResponse(HttpStatusCode.BadRequest, Unit)
        }
        if (password.length < 6) {
            ApiResponse(HttpStatusCode.BadRequest, Unit)
        }
        val result = transactionMaster {
            val result = useCase(SignUpUseCase.Param(name, email, password))
            result.either({
                rollback()
            }, {
                commit()
            })
            result
        }

        return result.apiResponse({
            ApiResponse(HttpStatusCode.BadRequest, Unit)
        }, {
            ApiResponse(AuthResponse(it.userId, jwtConfig.makeToken(it)))
        })
    }
}

fun SignUpController.inject() {
    val authRepository = AuthRepositoryImpl()
    useCase = SignUpUseCase(authRepository)
}
