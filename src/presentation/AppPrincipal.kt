package com.lab.clean.ktor.presentation

import io.ktor.auth.Principal

data class AppPrincipal constructor(val userId: String) : Principal