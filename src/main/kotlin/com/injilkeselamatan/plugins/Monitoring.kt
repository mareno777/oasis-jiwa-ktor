package com.injilkeselamatan.plugins

import ch.qos.logback.classic.LoggerContext
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO
        val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
        val rootLogger = loggerContext.getLogger("org.mongodb.driver")
        rootLogger.level = ch.qos.logback.classic.Level.ERROR
        filter { call -> call.request.path().startsWith("/") }
    }
}
