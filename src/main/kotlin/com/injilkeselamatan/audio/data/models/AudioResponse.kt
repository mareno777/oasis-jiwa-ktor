package com.injilkeselamatan.audio.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AudioResponse(
    val mediaId: String,
    val title: String,
    val artist: String,
    val songUrl: String,
    val imageUrl: String,
    val description: String?,
    val synopsis: String?
)
