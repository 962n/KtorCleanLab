package com.lab.clean.ktor.presentation.ui.signUp

import com.lab.clean.ktor.ApiResponse
import com.lab.clean.ktor.data.JwtConfig
import com.lab.clean.ktor.domain.AtomicProcessor
import com.lab.clean.ktor.domain.useCase.auth.SignUpUseCase
import com.lab.clean.ktor.presentation.ui.BaseController
import com.lab.clean.ktor.presentation.extension.apiResponse
import com.lab.clean.ktor.presentation.extension.isEmail
import com.lab.clean.ktor.presentation.response.AuthResponse
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.util.KtorExperimentalAPI

class SignUpController
@KtorExperimentalAPI
constructor(
    private val param: InputParam,
    private val atomicProcessor: AtomicProcessor,
    private val useCase: SignUpUseCase
) : BaseController() {

    data class InputParam(
        val name: String?,
        val email: String?,
        val password: String?
    )

    @KtorExperimentalAPI
    @KtorExperimentalLocationsAPI
    override suspend fun execute(): ApiResponse {
        val email = param.email ?: return ApiResponse(HttpStatusCode.BadRequest, Unit)
        val password = param.password ?: return ApiResponse(HttpStatusCode.BadRequest, Unit)
        val name = param.name ?: return ApiResponse(HttpStatusCode.BadRequest, Unit)
        if (!email.isEmail()) {
            return ApiResponse(HttpStatusCode.BadRequest, Unit)
        }
        if (password.length < 6) {
            return ApiResponse(HttpStatusCode.BadRequest, Unit)
        }

        val result = atomicProcessor.readWrite {
            useCase(SignUpUseCase.Param(name, email, password))
        }

        return result.apiResponse({
            ApiResponse(HttpStatusCode.BadRequest, Unit)
        }, {
            ApiResponse(AuthResponse(it.userId, it.token))
        })
    }
}