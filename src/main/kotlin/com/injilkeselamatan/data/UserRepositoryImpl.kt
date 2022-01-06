package com.injilkeselamatan.data

import com.injilkeselamatan.data.models.CreateUserRequest
import com.injilkeselamatan.data.models.UpdateUserRequest
import com.injilkeselamatan.data.models.UserResponse
import com.injilkeselamatan.helper.OperationsException
import com.injilkeselamatan.helper.UserAlreadyExists
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.or


class UserRepositoryImpl(private val db: CoroutineDatabase) : UserRepository {
    override suspend fun getAllUsers(): List<UserResponse> {
        return db.getCollection<UserResponse>("users")
            .find()
            .ascendingSort(UserResponse::createdAt)
            .toList()
    }

    override suspend fun getUserByPhoneNumber(phoneNumber: String): UserResponse {
        return db.getCollection<UserResponse>("users").findOne(filter = "{phoneNumber: '$phoneNumber'}")
            ?: throw NoSuchElementException("User doesn't exists")
    }

    override suspend fun createUser(createUserRequest: CreateUserRequest): UserResponse {
        val existedUser = db.getCollection<UserResponse>("users")
            .findOne(
                or(
                    UserResponse::email eq createUserRequest.email,
                    UserResponse::phoneNumber eq createUserRequest.phoneNumber
                )
            )
        if (existedUser != null) throw UserAlreadyExists("User already exists!")
        val successful = db.getCollection<CreateUserRequest>("users").insertOne(createUserRequest).wasAcknowledged()
        if (successful) return createUserRequest.toUserResponse()
        throw UserAlreadyExists()
    }

    override suspend fun updateUser(phoneNumber: String, updateUserRequest: UpdateUserRequest): UserResponse {
        val userId = db.getCollection<UserResponse>("users")
            .findOne("{phoneNumber: '$phoneNumber'}")?._id ?: throw NoSuchElementException("User doesn't exists")
        val isSuccessful =
            db.getCollection<UserResponse>("users").updateOneById(userId, updateUserRequest).wasAcknowledged()
        if (!isSuccessful) throw OperationsException()
        return updateUserRequest.toUserResponse(userId)
    }

    override suspend fun deleteUser(id: String): String {
        TODO("Not yet implemented")
    }
}