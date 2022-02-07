package com.injilkeselamatan.version.data

import com.injilkeselamatan.version.data.models.VersionResponse
import org.litote.kmongo.coroutine.CoroutineDatabase

class VersionRepositoryImpl(private val db: CoroutineDatabase) : VersionRepository {

    override suspend fun getVersionCode(): VersionResponse {
        return db.getCollection<VersionResponse>("configuration")
            .find()
            .ascendingSort(VersionResponse::releaseDate)
            .first()
            ?: throw NoSuchElementException()
    }
}