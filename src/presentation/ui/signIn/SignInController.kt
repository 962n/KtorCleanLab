package com.lab.clean.ktor.presentation.ui.signIn

import com.lab.clean.ktor.ApiResponse
import com.lab.clean.ktor.SignIn
import com.lab.clean.ktor.data.JwtConfig
import com.lab.clean.ktor.data.repositoryImpl.AuthRepositoryImpl
import com.lab.clean.ktor.domain.useCase.auth.SignInUseCase
import com.lab.clean.ktor.presentation.extension.apiResponse
import com.lab.clean.ktor.presentation.response.AuthResponse
import com.lab.clean.ktor.presentation.ui.BaseController
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.util.KtorExperimentalAPI
import org.jetbrains.exposed.sql.transactions.transaction

class SignInController
@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
constructor(
    private val param: SignIn,
    private val jwtConfig: JwtConfig
) : BaseController() {

    lateinit var useCase: SignInUseCase

    @KtorExperimentalAPI
    @KtorExperimentalLocationsAPI
    override suspend fun execute(): ApiResponse {
        inject()
        val email = param.email ?: return ApiResponse(HttpStatusCode.BadRequest, Unit)
        val password = param.password ?: return ApiResponse(HttpStatusCode.BadRequest, Unit)

        val result = transaction {
            useCase(SignInUseCase.Param(email, password))
        }

        return result.apiResponse({
            ApiResponse(HttpStatusCode.BadRequest, Unit)
        }, {
            ApiResponse(AuthResponse(it.userId, jwtConfig.makeToken(it)))
        })
    }
}
fun SignInController.inject() {
    useCase = SignInUseCase(AuthRepositoryImpl())
}