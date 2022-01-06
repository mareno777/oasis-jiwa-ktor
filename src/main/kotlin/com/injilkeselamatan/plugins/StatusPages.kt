package com.injilkeselamatan.plugins

import com.injilkeselamatan.data.models.UserResponse
import com.injilkeselamatan.data.models.WebResponse
import com.injilkeselamatan.helper.OperationsException
import com.injilkeselamatan.helper.UserAlreadyExists
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureStatusPages() {
    routing {
        install(StatusPages) {
            exception<UserAlreadyExists> { cause ->
                val httpStatusCode = HttpStatusCode.MethodNotAllowed
                call.respond(
                    httpStatusCode, WebResponse(
                        code = httpStatusCode.value,
                        data = emptyList<UserResponse>(),
                        message = cause.message ?: "Unknown error occurred"
                    )
                )
            }
            exception<NoSuchElementException> { cause ->
                val httpStatusCode = HttpStatusCode.NotFound
                call.respond(
                    httpStatusCode, WebResponse(
                        code = httpStatusCode.value,
                        data = emptyList<UserResponse>(),
                        message = cause.message ?: "Unknown error occurred"
                    )
                )
            }
            exception<OperationsException> { cause ->
                val httpStatusCode = HttpStatusCode.MethodNotAllowed
                call.respond(
                    httpStatusCode, WebResponse(
                        code = httpStatusCode.value,
                        data = emptyList<UserResponse>(),
                        message = cause.message ?: "Unknown error occurred"
                    )
                )
            }
        }
    }
}