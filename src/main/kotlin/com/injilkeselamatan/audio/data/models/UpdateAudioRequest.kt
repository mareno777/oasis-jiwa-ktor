package com.injilkeselamatan.audio.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UpdateAudioRequest(
    val title: String,
    val artist: String,
    val album: String,
    val songUrl: String,
    val imageUrl: String,
    val description: String?,
    val synopsis: String? = null,
    val duration: Long = 0L,
    val uploadedAt: Long
) {
    fun toAudioResponse(mediaId: String): AudioResponse {
        return AudioResponse(
            mediaId = mediaId,
            title = title,
            artist = artist,
            album = album,
            songUrl = songUrl,
            imageUrl = imageUrl,
            description = description,
            synopsis = synopsis,
            duration = duration,
            uploadedAt = uploadedAt
        )
    }
}
