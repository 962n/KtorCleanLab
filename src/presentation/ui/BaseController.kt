package com.lab.clean.ktor.presentation.ui

import com.lab.clean.ktor.ApiResponse

abstract class BaseController {

    abstract fun execute(): ApiResponse
}