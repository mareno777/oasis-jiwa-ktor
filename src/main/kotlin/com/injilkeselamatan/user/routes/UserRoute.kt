package com.injilkeselamatan.user.routes

import com.injilkeselamatan.user.data.UserRepository
import com.injilkeselamatan.user.data.models.CreateUserRequest
import com.injilkeselamatan.user.data.models.UpdateUserRequest
import com.injilkeselamatan.user.data.models.WebResponse
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
        // update current user by phone number
        put("/{phoneNumber}") {
            val updateUserRequest = call.receive<UpdateUserRequest>()
            val phoneNumber = call.parameters["phoneNumber"] ?: throw IllegalArgumentException("Phone number is blank!")
            val response = userRepository.updateUser(phoneNumber, updateUserRequest)
            val httpStatus = HttpStatusCode.OK
            call.respond(httpStatus, WebResponse(httpStatus.value, response, "User successfully updated"))
        }
        // delete current user by Id
        delete {

        }
    }
}