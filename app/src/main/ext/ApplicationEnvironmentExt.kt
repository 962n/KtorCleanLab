package com.lab.clean.ktor.app.ext

import com.lab.clean.ktor.data.database.DBConnector
import com.lab.clean.ktor.data.jwt.JwtService
import io.ktor.application.ApplicationEnvironment
import io.ktor.util.KtorExperimentalAPI

@KtorExperimentalAPI
fun ApplicationEnvironment.jwtConfig() : JwtService.Config {
    val issuer = config.property("jwt.issuer").getString()
    val realm = config.property("jwt.realm").getString()
    val secret = config.property("jwt.secret").getString()
    val audience = config.property("jwt.audience").getString()
    val validityInMin = config.property("jwt.validity_in_min").getString().toInt()
    return JwtService.Config(issuer,realm,secret,audience,validityInMin)
}

@KtorExperimentalAPI
fun ApplicationEnvironment.slaveDBConfig() : DBConnector.Config {
    val maximumPoolSize = config
        .property("database.slave.pool_size")
        .getString()
        .toInt()

    val username = config
        .property("database.slave.username")
        .getString()

    val password = config
        .property("database.slave.password")
        .getString()

    val driverName = config
        .property("database.slave.driver_name")
        .getString()

    val jdbcUrl = config
        .property("database.slave.jdbc_url")
        .getString()
    return DBConnector.Config(username,password,maximumPoolSize,driverName,jdbcUrl)
}
@KtorExperimentalAPI
fun ApplicationEnvironment.masterDBConfig() : DBConnector.Config {
    val maximumPoolSize = config
        .property("database.master.pool_size")
        .getString()
        .toInt()

    val username = config
        .property("database.master.username")
        .getString()

    val password = config
        .property("database.master.password")
        .getString()

    val driverName = config
        .property("database.master.driver_name")
        .getString()

    val jdbcUrl = config
        .property("database.master.jdbc_url")
        .getString()
    return DBConnector.Config(username,password,maximumPoolSize,driverName,jdbcUrl)
}