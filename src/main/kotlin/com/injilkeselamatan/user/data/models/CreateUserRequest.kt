package com.injilkeselamatan.user.data.models

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class CreateUserRequest(
    @BsonId
    val id: Id<String> = newId(),
    val email: String,
    val name: String,
    val phoneNumber: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long? = null,
    val ipAddress: String,
    val lastLogin: Long,
    val model: String,
    val profile: String
) {
    fun toUserResponse(): UserResponse {
        return UserResponse(
            _id = id.toString(),
            email = email,
            name = name,
            phoneNumber = phoneNumber,
            createdAt = createdAt,
            updatedAt = updatedAt,
            ipAddress = ipAddress,
            lastLogin = lastLogin,
            model = model,
            profile = profile
        )
    }
}
