package com.injilkeselamatan

import com.injilkeselamatan.di.mainModule
import com.injilkeselamatan.plugins.configureMonitoring
import com.injilkeselamatan.plugins.configureRouting
import com.injilkeselamatan.plugins.configureSerialization
import com.injilkeselamatan.plugins.configureStatusPages
import io.ktor.application.*
import io.ktor.application.*
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.modules


fun main(args: Array<String>): Unit {
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.user() {
    install(Koin) {
        modules(mainModule)
    }
    configureRouting()
    configureMonitoring()
    configureSerialization()
    configureStatusPages()
}

fun Application.audio() {

}
