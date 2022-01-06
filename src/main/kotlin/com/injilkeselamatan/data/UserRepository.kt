package com.injilkeselamatan.data

import com.injilkeselamatan.data.models.CreateUserRequest
import com.injilkeselamatan.data.models.UpdateUserRequest
import com.injilkeselamatan.data.models.UserResponse

interface UserRepository {

    suspend fun getAllUsers(): List<UserResponse>

    suspend fun getUserByPhoneNumber(phoneNumber: String): UserResponse

    suspend fun createUser(createUserRequest: CreateUserRequest): UserResponse

    suspend fun updateUser(phoneNumber: String, updateUserRequest: UpdateUserRequest): UserResponse

    suspend fun deleteUser(phoneNumber: String): String
}