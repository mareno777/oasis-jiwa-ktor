package com.injilkeselamatan.user.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequest(
    val email: String,
    val name: String,
    val phoneNumber: String,
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun toUserResponse(userId: String, createdAt: Long?): UserResponse {
        return UserResponse(
            _id = userId,
            email = email,
            phoneNumber = phoneNumber,
            name = name,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
