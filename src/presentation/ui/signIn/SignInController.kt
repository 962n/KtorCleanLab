package com.lab.clean.ktor.presentation.ui.signIn

import com.lab.clean.ktor.ApiResponse
import com.lab.clean.ktor.SignIn
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

        val result = useCase(SignInUseCase.Param(param.email, param.password))

        return result.apiResponse({
            ApiResponse(HttpStatusCode.OK, Unit)
        }, {
            ApiResponse(HttpStatusCode.OK, Unit)
        })

    }
}