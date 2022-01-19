package com.injilkeselamatan.user.data

import com.injilkeselamatan.helper.OperationsException
import com.injilkeselamatan.helper.ResourceAlreadyExists
import com.injilkeselamatan.user.data.models.CreateUserRequest
import com.injilkeselamatan.user.data.models.UpdateUserRequest
import com.injilkeselamatan.user.data.models.UserResponse
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.or
import org.litote.kmongo.set
import org.litote.kmongo.setTo

class UserRepositoryImpl(private val db: CoroutineDatabase) :  UserRepository {
    override suspend fun getAllUsers(): List<UserResponse> {
        return db.getCollection<UserResponse>("users")
            .find()
            .ascendingSort(UserResponse::createdAt)
            .toList()
    }

    override suspend fun getUserByEmail(email: String): UserResponse {
        return db.getCollection<UserResponse>("users").findOne(filter = "{email: '$email'}")
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
        if (existedUser != null) throw ResourceAlreadyExists("User already exists!")
        val successful = db.getCollection<CreateUserRequest>("users")
            .insertOne(createUserRequest).wasAcknowledged()
        if (successful) return createUserRequest.toUserResponse()
        throw OperationsException()
    }

    override suspend fun updateUser(email: String, updateUserRequest: UpdateUserRequest): UserResponse {
        val result = db.getCollection<UserResponse>("users")
            .findOneAndUpdate(
                filter = UserResponse::email eq email,
                set(
                    UserResponse::name setTo updateUserRequest.name,
                    UserResponse::email setTo updateUserRequest.email,
                    UserResponse::updatedAt setTo updateUserRequest.updatedAt,
                    UserResponse::phoneNumber setTo updateUserRequest.phoneNumber,
                    UserResponse::ipAddress setTo updateUserRequest.ipAddress,
                    UserResponse::lastLogin setTo updateUserRequest.lastLogin,
                    UserResponse::model setTo updateUserRequest.model,
                    UserResponse::profile setTo updateUserRequest.profile
                )
            ) ?: throw NoSuchElementException("User not found!")
        return updateUserRequest.toUserResponse(result._id, result.createdAt)
    }

    override suspend fun deleteUser(email: String) {
        db.getCollection<UserResponse>("users")
            .findOneAndDelete(
                filter = UserResponse::email eq email,
            ) ?: throw NoSuchElementException("Users not found!")
    }
}