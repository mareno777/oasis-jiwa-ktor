package com.injilkeselamatan.helper

import kotlinx.serialization.Serializable

@Serializable
data class WebResponse<T>(
    val code: Int,
    val data: T,
    val message: String
)
