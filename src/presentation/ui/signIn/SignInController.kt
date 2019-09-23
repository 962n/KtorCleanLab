package com.lab.clean.ktor.presentation.ui.signIn

import com.lab.clean.ktor.ApiResponse
import com.lab.clean.ktor.SignIn
import com.lab.clean.ktor.domain.entity.auth.AuthEntity
import com.lab.clean.ktor.domain.useCase.auth.SignInUseCase
import com.lab.clean.ktor.presentation.extension.apiResponse
import com.lab.clean.ktor.presentation.ui.BaseController
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI

class SignInController
@KtorExperimentalLocationsAPI
constructor(
    private val param: SignIn
) : BaseController() {

    lateinit var useCase: SignInUseCase

    @KtorExperimentalLocationsAPI
    override fun execute(): ApiResponse {
        if (true) {
            return ApiResponse(HttpStatusCode.OK,"sign in success")
        }
        val email = param.email ?: ""
        val password = param.password ?: ""
        // TODO validate

        val result = useCase(SignInUseCase.Param(email, password))

        return result.apiResponse({
            ApiResponse(Unit)
        }, {
            ApiResponse(Unit)
        })

    }
}