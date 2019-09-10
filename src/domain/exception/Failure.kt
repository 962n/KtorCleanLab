package com.lab.clean.ktor.domain.exception

sealed class Failure {
    abstract class FeatureFailure : Failure()
}