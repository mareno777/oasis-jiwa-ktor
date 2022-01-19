package com.injilkeselamatan.user.data

import com.injilkeselamatan.user.data.models.CreateUserRequest
import com.injilkeselamatan.user.data.models.UpdateUserRequest
import com.injilkeselamatan.user.data.models.UserResponse

interface UserRepository {

    suspend fun getAllUsers(): List<UserResponse>

    suspend fun getUserByEmail(email: String): UserResponse

    suspend fun createUser(createUserRequest: CreateUserRequest): UserResponse

    suspend fun updateUser(email: String, updateUserRequest: UpdateUserRequest): UserResponse

    suspend fun deleteUser(email: String)
}