package com.injilkeselamatan.user.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequest(
    val email: String,
    val name: String,
    val phoneNumber: String,
    val updatedAt: Long = System.currentTimeMillis(),
    val ipAddress: String,
    val lastLogin: Long = System.currentTimeMillis(),
    val model: String,
    val profile: String
) {
    fun toUserResponse(userId: String, createdAt: Long?): UserResponse {
        return UserResponse(
            _id = userId,
            email = email,
            phoneNumber = phoneNumber,
            name = name,
            createdAt = createdAt,
            updatedAt = updatedAt,
            ipAddress = ipAddress,
            lastLogin = lastLogin,
            model = model,
            profile = profile
        )
    }
}
