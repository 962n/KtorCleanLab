package com.lab.clean.ktor.route

import com.lab.clean.ktor.data.AtomicProcessorImpl
import com.lab.clean.ktor.data.JwtConfig
import com.lab.clean.ktor.data.TokenGeneratorImpl
import com.lab.clean.ktor.data.repositoryImpl.AuthRepositoryImpl
import com.lab.clean.ktor.domain.useCase.auth.SignInUseCase
import com.lab.clean.ktor.domain.useCase.auth.SignUpUseCase
import com.lab.clean.ktor.presentation.extension.respondApi
import com.lab.clean.ktor.presentation.ui.signIn.SignInController
import com.lab.clean.ktor.presentation.ui.signUp.SignUpController
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.request.receiveParameters
import io.ktor.routing.Routing
import io.ktor.util.KtorExperimentalAPI

@KtorExperimentalLocationsAPI
@Location("/sign_up")
data class SignUp(
    val name: String? = null,
    val email: String? = null,
    val password: String? = null
) {
    fun toControllerInput(): SignUpController.InputParam {
        return SignUpController.InputParam(name, email, password)
    }
}

@KtorExperimentalLocationsAPI
private suspend fun ApplicationCall.getSignUpParam(): SignUp {
    val parameters = this.receiveParameters()
    return SignUp(
        parameters.get("name"),
        parameters.get("email"),
        parameters.get("password")
    )
}

@KtorExperimentalLocationsAPI
private suspend fun ApplicationCall.getSignInParam(): SignIn {
    val parameters = this.receiveParameters()
    return SignIn(
        parameters.get("email"),
        parameters.get("password")
    )
}

@KtorExperimentalLocationsAPI
@Location("/sign_in")
data class SignIn(
    val email: String? = null,
    val password: String? = null
) {
    fun toControllerInput(): SignInController.InputParam {
        return SignInController.InputParam(email, password)
    }
}

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun Routing.routingAuth(jwtConfig: JwtConfig) {
    post<SignUp> {
        val useCase = SignUpUseCase(AuthRepositoryImpl(), TokenGeneratorImpl(jwtConfig))
        val controller = SignUpController(
            call.getSignUpParam().toControllerInput(),
            AtomicProcessorImpl,
            useCase
        )
        call.respondApi(controller)
    }
    post<SignIn> {
        val useCase = SignInUseCase(AuthRepositoryImpl(), TokenGeneratorImpl(jwtConfig))
        val controller = SignInController(
            call.getSignInParam().toControllerInput(),
            AtomicProcessorImpl,
            useCase
        )
        call.respondApi(controller)
    }
}