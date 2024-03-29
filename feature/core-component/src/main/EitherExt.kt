package com.lab.clean.ktor.feature.coreComponent

import com.lab.clean.ktor.domain.core.Either

fun <L, R> Either<L, R>.apiResponse(fL: (L) -> ApiResponse, fR: (R) -> ApiResponse) =
    when (this) {
        is Either.Left -> fL(a)
        is Either.Right -> fR(b)
    }