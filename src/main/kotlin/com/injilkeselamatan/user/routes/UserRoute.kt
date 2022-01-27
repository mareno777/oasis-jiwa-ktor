package com.injilkeselamatan.user.routes

import com.injilkeselamatan.helper.WebResponse
import com.injilkeselamatan.user.data.UserRepository
import com.injilkeselamatan.user.data.models.CreateUserRequest
import com.injilkeselamatan.user.data.models.UpdateUserRequest
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
        // get user by phone number
        get("{email}") {
            val phoneNumber = call.parameters["email"] ?: return@get
            val response = userRepository.getUserByEmail(phoneNumber)
            val httpStatus = HttpStatusCode.OK
            call.respond(httpStatus, WebResponse(httpStatus.value, response, httpStatus.description))
        }
        // create a new user
        post {
            var createUserRequest = call.receive<CreateUserRequest>()
            createUserRequest = createUserRequest.copy(
                createdAt = System.currentTimeMillis(),
                lastLogin = System.currentTimeMillis()
            )

            val response = userRepository.createUser(createUserRequest)
            val httpStatus = HttpStatusCode.Created
            call.respond(httpStatus, WebResponse(httpStatus.value, response, httpStatus.description))
        }
        // update current user by phone number
        put("/{email}") {
            var updateUserRequest = call.receive<UpdateUserRequest>()
            updateUserRequest = updateUserRequest.copy(
                lastLogin = System.currentTimeMillis()
            )

            val email = call.parameters["email"] ?: throw IllegalArgumentException("Email is blank!")
            val response = userRepository.updateUser(email, updateUserRequest)
            val httpStatus = HttpStatusCode.OK
            call.respond(httpStatus, WebResponse(httpStatus.value, response, "User successfully updated"))
        }
        // delete current user by phone number
        delete("/{email}") {
            val phoneNumber = call.parameters["email"] ?: throw IllegalArgumentException("Email is blank!")
            userRepository.deleteUser(phoneNumber)
            val httpStatus = HttpStatusCode.OK
            call.respond(
                httpStatus,
                WebResponse(httpStatus.value, "", "User successfully deleted")
            )
        }
    }
}