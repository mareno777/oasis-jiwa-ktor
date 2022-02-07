package com.injilkeselamatan.version.data.models

import kotlinx.serialization.Serializable

@Serializable
data class VersionResponse(
    val version: Int,
    val releaseDate: Long
)
