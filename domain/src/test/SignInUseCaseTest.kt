package com.lab.clean.ktor.domain

import com.lab.clean.ktor.domain.core.Either
import com.lab.clean.ktor.domain.entity.auth.AuthEntity
import com.lab.clean.ktor.domain.exception.SignUpFailure
import com.lab.clean.ktor.domain.exception.SignUpFailureType
import com.lab.clean.ktor.domain.repository.auth.AuthRepository
import com.lab.clean.ktor.domain.repository.auth.TokenGenerator
import com.lab.clean.ktor.domain.useCase.auth.SignInUseCase
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class SignInUseCaseTest {

    private val authRepository = mock<AuthRepository> { }
    private val tokenGenerator = mock<TokenGenerator> {}
    private val useCase by lazy {
        SignInUseCase(authRepository, tokenGenerator)
    }

    @Test
    fun `run success`() {
        val email = ""
        val password = ""
        val expect = AuthEntity(1,"expectToken")
        given {
            authRepository.signIn(email, password)
        }.willReturn(Either.Right(expect.userId))
        given {
            tokenGenerator.generate(expect.userId)
        }.willReturn(expect.token)
        val result = useCase(SignInUseCase.Param(email, password))
        result.either({
            fail("do not expect this route")
        }, { actual ->
            assertEquals(expect.userId, actual.userId)
            assertEquals(expect.token, actual.token)
        })
        verify(authRepository).signIn(email, password)
        verify(tokenGenerator).generate(expect.userId)
    }

    @Test
    fun `run failure`() {
        val email = ""
        val password = ""
        val expect = SignUpFailure(SignUpFailureType.ALREADY_EXIST)
        given {
            authRepository.signIn(email, password)
        }.willReturn(Either.Left(expect))
        val result = useCase(SignInUseCase.Param(email, password))
        result.either({
            assertTrue(it is SignUpFailure)
        }, {
            fail("do not expect this route")
        })
        verify(authRepository).signIn(email, password)
    }
}