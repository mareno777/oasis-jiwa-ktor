package com.injilkeselamatan.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequest(
    val email: String,
    val name: String,
    val phoneNumber: String
) {
    fun toUserResponse(userId: String): UserResponse {
        return UserResponse(
            _id = userId,
            email = email,
            phoneNumber = phoneNumber,
            name = name,
            createdAt = null,
            updatedAt = System.currentTimeMillis()
        )
    }
}
