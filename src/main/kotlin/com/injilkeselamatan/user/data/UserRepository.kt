package com.injilkeselamatan.user.data

import com.injilkeselamatan.user.data.models.CreateUserRequest
import com.injilkeselamatan.user.data.models.UpdateUserRequest
import com.injilkeselamatan.user.data.models.UserResponse

interface UserRepository {

    suspend fun getAllUsers(): List<UserResponse>

    suspend fun getUserByPhoneNumber(phoneNumber: String): UserResponse

    suspend fun createUser(createUserRequest: CreateUserRequest): UserResponse

    suspend fun updateUser(phoneNumber: String, updateUserRequest: UpdateUserRequest): UserResponse

    suspend fun deleteUser(phoneNumber: String)
}