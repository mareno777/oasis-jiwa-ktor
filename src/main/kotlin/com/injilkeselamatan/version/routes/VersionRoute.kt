package com.injilkeselamatan.version.routes

import com.injilkeselamatan.helper.WebResponse
import com.injilkeselamatan.version.data.VersionRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.versionRouting() {
    val versionRepository by inject<VersionRepository>()

    routing {
        route("/version") {
            get {
                val response = versionRepository.getVersionCode()
                val httpStatus = HttpStatusCode.OK
                call.respond(httpStatus, WebResponse(httpStatus.value, response, httpStatus.description))
            }
        }
        route("/http-version") {
            get {
                call.respondText("HTTP VERSION IS ${call.request.httpVersion}")
            }
        }
    }
}