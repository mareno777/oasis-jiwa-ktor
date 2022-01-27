package com.injilkeselamatan.audio.data.models

import kotlinx.serialization.Serializable

@Serializable
data class FeaturedAudioResponse(
    val mediaId: String,
    val title: String,
    val album: String,
    val artist: String,
    val songUrl: String,
    val imageUrl: String,
    val description: String?,
    val synopsis: String?,
    val duration: Long?,
    val uploadedAt: Long,
    val displayedAt: String
)
