package com.lab.clean.ktor.feature.coreComponent

abstract class BaseController {

    abstract suspend fun execute(): ApiResponse
}