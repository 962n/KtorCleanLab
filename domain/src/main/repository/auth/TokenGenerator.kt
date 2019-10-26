package com.lab.clean.ktor.domain.repository.auth

interface TokenGenerator {
    fun generate(userId:Int):String
}