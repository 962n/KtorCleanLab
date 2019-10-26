package com.lab.clean.ktor.feature.auth.signUp

import com.lab.clean.ktor.domain.Transactor
import com.lab.clean.ktor.domain.useCase.auth.SignUpUseCase
import com.lab.clean.ktor.feature.auth.AuthResponse
import com.lab.clean.ktor.feature.auth.isEmail
import com.lab.clean.ktor.feature.coreComponent.ApiResponse
import com.lab.clean.ktor.feature.coreComponent.ApiStatus
import com.lab.clean.ktor.feature.coreComponent.BaseController
import com.lab.clean.ktor.feature.coreComponent.apiResponse

class SignUpController
constructor(
    private val param: InputParam,
    private val transactor: Transactor,
    private val useCase: SignUpUseCase
) : BaseController() {

    data class InputParam(
        val name: String?,
        val email: String?,
        val password: String?
    )

    override suspend fun execute(): ApiResponse {
        val email = param.email ?: return ApiResponse(ApiStatus.BAD_PARAMETER, Unit)
        val password = param.password ?: return ApiResponse(ApiStatus.BAD_PARAMETER, Unit)
        val name = param.name ?: return ApiResponse(ApiStatus.BAD_PARAMETER, Unit)
        if (!email.isEmail()) {
            return ApiResponse(ApiStatus.BAD_PARAMETER, Unit)
        }
        if (password.length < 6) {
            return ApiResponse(ApiStatus.BAD_PARAMETER, Unit)
        }

        val result = transactor.readWrite {
            useCase(SignUpUseCase.Param(name, email, password))
        }

        return result.apiResponse({
            ApiResponse(ApiStatus.BAD_PARAMETER, Unit)
        }, {
            ApiResponse(AuthResponse(it.userId, it.token))
        })
    }
}