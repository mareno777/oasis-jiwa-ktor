package com.injilkeselamatan.audio.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UpdateAudioRequest(
    val title: String,
    val artist: String,
    val songUrl: String,
    val imageUrl: String,
    val description: String?,
    val synopsis: String?
) {
    fun toAudioResponse(mediaId: String): AudioResponse {
        return AudioResponse(
            mediaId = mediaId,
            title = title,
            artist = artist,
            songUrl = songUrl,
            imageUrl = imageUrl,
            description = description,
            synopsis = synopsis
        )
    }
}
