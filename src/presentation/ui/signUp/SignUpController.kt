package com.lab.clean.ktor.presentation.ui.signUp

import com.lab.clean.ktor.ApiResponse
import com.lab.clean.ktor.SignUp
import com.lab.clean.ktor.domain.useCase.auth.SignUpUseCase
import com.lab.clean.ktor.presentation.ui.BaseController
import com.lab.clean.ktor.presentation.extension.apiResponse
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI

class SignUpController
@KtorExperimentalLocationsAPI
constructor(
    private val param: SignUp
) : BaseController() {

    lateinit var useCase: SignUpUseCase

    @KtorExperimentalLocationsAPI
    override fun execute(): ApiResponse {
        if (true) {
            return ApiResponse("sign in success")
        }
        val email = param.email ?: ""
        val password = param.password ?: ""
        // TODO validate

        val result = useCase(SignUpUseCase.Param(email, password))
        return result.apiResponse({
            ApiResponse(Unit)
        }, {
            ApiResponse(Unit)
        })
    }
}