package com.lab.clean.ktor.feature.auth.signIn

import com.lab.clean.ktor.domain.Transactor
import com.lab.clean.ktor.domain.useCase.auth.SignInUseCase
import com.lab.clean.ktor.feature.auth.AuthResponse
import com.lab.clean.ktor.feature.coreComponent.ApiResponse
import com.lab.clean.ktor.feature.coreComponent.ApiStatus
import com.lab.clean.ktor.feature.coreComponent.BaseController
import com.lab.clean.ktor.feature.coreComponent.apiResponse

class SignInController
constructor(
    private val param: InputParam,
    private val transactor: Transactor,
    private val useCase: SignInUseCase
) : BaseController() {
    data class InputParam(val email: String?, val password: String?)

    override suspend fun execute(): ApiResponse {
        val email = param.email ?: return ApiResponse(ApiStatus.BAD_PARAMETER, Unit)
        val password = param.password ?: return ApiResponse(ApiStatus.BAD_PARAMETER, Unit)

        val result = transactor.readOnly {
            useCase(SignInUseCase.Param(email, password))
        }

        return result.apiResponse({
            ApiResponse(ApiStatus.BAD_PARAMETER, Unit)
        }, {
            ApiResponse(AuthResponse(it.userId, it.token))
        })
    }
}
