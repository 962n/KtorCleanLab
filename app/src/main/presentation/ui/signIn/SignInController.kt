package com.lab.clean.ktor.presentation.ui.signIn

import com.lab.clean.ktor.ApiResponse
import com.lab.clean.ktor.domain.AtomicProcessor
import com.lab.clean.ktor.domain.useCase.auth.SignInUseCase
import com.lab.clean.ktor.presentation.extension.apiResponse
import com.lab.clean.ktor.presentation.response.AuthResponse
import com.lab.clean.ktor.presentation.ui.BaseController
import io.ktor.http.HttpStatusCode

class SignInController
constructor(
    private val param: InputParam,
    private val atomicProcessor: AtomicProcessor,
    private val useCase: SignInUseCase
) : BaseController() {
    data class InputParam(val email: String?, val password: String?)

    override suspend fun execute(): ApiResponse {
        val email = param.email ?: return ApiResponse(HttpStatusCode.BadRequest, Unit)
        val password = param.password ?: return ApiResponse(HttpStatusCode.BadRequest, Unit)

        val result = atomicProcessor.readOnly {
            useCase(SignInUseCase.Param(email, password))
        }

        return result.apiResponse({
            ApiResponse(HttpStatusCode.BadRequest, Unit)
        }, {
            ApiResponse(AuthResponse(it.userId, it.token))
        })
    }
}
