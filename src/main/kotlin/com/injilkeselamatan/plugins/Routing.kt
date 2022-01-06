package com.injilkeselamatan.plugins

import com.injilkeselamatan.routes.userRouting
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureRouting() {

    routing() {
        route("/") {
            get {
                call.respondRedirect("https://injilkeselamatan.com")
            }
        }
        userRouting()
    }
}