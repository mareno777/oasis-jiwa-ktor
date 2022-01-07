package com.injilkeselamatan.audio.routes

import com.injilkeselamatan.audio.data.AudioRepository
import com.injilkeselamatan.audio.data.models.CreateAudioRequest
import com.injilkeselamatan.audio.data.models.UpdateAudioRequest
import com.injilkeselamatan.helper.WebResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.audioRouting() {

    val audioRepository by inject<AudioRepository>()

    routing {
        route("/audio") {
            get {
                val response = audioRepository.getAllAudio()
                val httpStatus = HttpStatusCode.OK
                call.respond(httpStatus, WebResponse(httpStatus.value, response, httpStatus.description))
            }
            get("/{mediaId}") {
                val mediaId = call.parameters["mediaId"] ?: return@get
                val response = audioRepository.getAudio(mediaId)
                val httpStatus = HttpStatusCode.OK
                call.respond(httpStatus, WebResponse(httpStatus.value, response, httpStatus.description))
            }
            post {
                val createAudioRequest = call.receive<CreateAudioRequest>()
                val response = audioRepository.createAudio(createAudioRequest)
                val httpStatus = HttpStatusCode.Created
                call.respond(httpStatus, WebResponse(httpStatus.value, response, httpStatus.description))
            }
            put("/{mediaId}") {
                val updateAudioRequest = call.receive<UpdateAudioRequest>()
                val mediaId = call.parameters["mediaId"] ?: throw IllegalArgumentException("Media Id is blank!")
                val response = audioRepository.updateAudio(mediaId, updateAudioRequest)
                val httpStatus = HttpStatusCode.OK
                call.respond(httpStatus, WebResponse(httpStatus.value, response, "Audio successfully updated"))
            }
            delete("/{mediaId}") {
                val mediaId = call.parameters["mediaId"] ?: throw IllegalArgumentException("Media Id is blank!")
                audioRepository.deleteAudio(mediaId)
                val httpStatus = HttpStatusCode.OK
                call.respond(
                    httpStatus,
                    WebResponse(httpStatus.value, "", "Audio successfully deleted")
                )
            }
        }
    }
}