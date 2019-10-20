package com.lab.clean.ktor.domain.exception

data class SignUpFailure constructor(val type:SignUpFailureType): Failure.FeatureFailure()
enum class SignUpFailureType {
    ALREADY_EXIST
}