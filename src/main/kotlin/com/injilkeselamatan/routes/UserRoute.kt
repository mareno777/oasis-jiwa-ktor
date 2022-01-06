package com.injilkeselamatan.routes

import com.injilkeselamatan.data.UserRepository
import com.injilkeselamatan.data.models.CreateUserRequest
import com.injilkeselamatan.data.models.WebResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Routing.userRouting() {

    val userRepository by inject<UserRepository>()

    route("/users") {
        get {
            val response = userRepository.getAllUsers()
            val httpStatus = HttpStatusCode.OK
            call.respond(httpStatus, WebResponse(httpStatus.value, response, httpStatus.description))
        }
        // get by user Id
        get("{phoneNumber}") {
            val phoneNumber = call.parameters["phoneNumber"] ?: return@get
            val response = userRepository.getUserByPhoneNumber(phoneNumber)
            val httpStatus = HttpStatusCode.OK
            call.respond(httpStatus, WebResponse(httpStatus.value, response, httpStatus.description))
        }
        // create new user
        post() {
            val createUserRequest = call.receive<CreateUserRequest>()
            val response = userRepository.createUser(createUserRequest)
            val httpStatus = HttpStatusCode.Created
            call.respond(httpStatus, WebResponse(httpStatus.value, response, httpStatus.description))
        }
        // update current user by Id
        put {

        }
        // delete current user by Id
        delete {

        }
    }
}