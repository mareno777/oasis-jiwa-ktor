package com.injilkeselamatan.audio.data

import com.injilkeselamatan.audio.data.models.AudioResponse
import com.injilkeselamatan.audio.data.models.CreateAudioRequest
import com.injilkeselamatan.audio.data.models.UpdateAudioRequest
import com.injilkeselamatan.helper.OperationsException
import com.injilkeselamatan.helper.ResourceAlreadyExists
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.or
import org.litote.kmongo.set
import org.litote.kmongo.setTo

class AudioRepositoryImpl(private val db: CoroutineDatabase) : AudioRepository {
    override suspend fun getAllAudio(): List<AudioResponse> {
        return db.getCollection<AudioResponse>("audio")
            .find()
            .ascendingSort(AudioResponse::mediaId)
            .toList()
    }

    override suspend fun getAudio(mediaId: String): AudioResponse {
        return db.getCollection<AudioResponse>("audio").findOne(filter = "{mediaId: '$mediaId'}")
            ?: throw NoSuchElementException("Audio doesn't exists")
    }

    override suspend fun createAudio(createAudioRequest: CreateAudioRequest): AudioResponse {
        val existedAudio = db.getCollection<AudioResponse>("audio")
            .findOne(
                or(
                    AudioResponse::mediaId eq createAudioRequest.mediaId,
                    AudioResponse::title eq createAudioRequest.title,
                    AudioResponse::songUrl eq createAudioRequest.songUrl
                )
            )
        if (existedAudio != null) throw ResourceAlreadyExists("Audio already exists!")
        val successful = db.getCollection<CreateAudioRequest>("audio")
            .insertOne(createAudioRequest).wasAcknowledged()
        if (successful) return createAudioRequest.toAudioResponse()
        throw OperationsException()
    }

    override suspend fun updateAudio(mediaId: String, updateAudioRequest: UpdateAudioRequest): AudioResponse {
        val result = db.getCollection<AudioResponse>("audio")
            .findOneAndUpdate(
                filter = AudioResponse::mediaId eq mediaId,
                set(
                    AudioResponse::title setTo updateAudioRequest.title,
                    AudioResponse::artist setTo updateAudioRequest.artist,
                    AudioResponse::songUrl setTo updateAudioRequest.songUrl,
                    AudioResponse::imageUrl setTo updateAudioRequest.imageUrl,
                    AudioResponse::description setTo updateAudioRequest.description,
                    AudioResponse::synopsis setTo updateAudioRequest.synopsis,
                )
            ) ?: throw NoSuchElementException("Audio not found!")
        return updateAudioRequest.toAudioResponse(result.mediaId)
    }

    override suspend fun deleteAudio(mediaId: String) {
        db.getCollection<AudioResponse>("audio")
            .findOneAndDelete(
                filter = AudioResponse::mediaId eq mediaId,
            ) ?: throw NoSuchElementException("Audio not found!")
    }

}