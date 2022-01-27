package com.injilkeselamatan.audio.data

import com.injilkeselamatan.audio.data.models.AudioResponse
import com.injilkeselamatan.audio.data.models.CreateAudioRequest
import com.injilkeselamatan.audio.data.models.FeaturedAudioResponse
import com.injilkeselamatan.audio.data.models.UpdateAudioRequest
import com.injilkeselamatan.helper.OperationsException
import com.injilkeselamatan.helper.ResourceAlreadyExists
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.aggregate
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*


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
        throw OperationsException("createAudio encounter a problem!")
    }

    override suspend fun updateAudio(mediaId: String, updateAudioRequest: UpdateAudioRequest): AudioResponse {
        val result = db.getCollection<AudioResponse>("audio")
            .findOneAndUpdate(
                filter = AudioResponse::mediaId eq mediaId,
                update = if (updateAudioRequest.description == null) set(
                    AudioResponse::title setTo updateAudioRequest.title,
                    AudioResponse::artist setTo updateAudioRequest.artist,
                    AudioResponse::songUrl setTo updateAudioRequest.songUrl,
                    AudioResponse::imageUrl setTo updateAudioRequest.imageUrl,
                    AudioResponse::synopsis setTo updateAudioRequest.synopsis,
                    AudioResponse::duration setTo updateAudioRequest.duration,
                    AudioResponse::uploadedAt setTo updateAudioRequest.uploadedAt
                ) else set(
                    AudioResponse::title setTo updateAudioRequest.title,
                    AudioResponse::artist setTo updateAudioRequest.artist,
                    AudioResponse::songUrl setTo updateAudioRequest.songUrl,
                    AudioResponse::imageUrl setTo updateAudioRequest.imageUrl,
                    AudioResponse::description setTo updateAudioRequest.description,
                    AudioResponse::synopsis setTo updateAudioRequest.synopsis,
                    AudioResponse::duration setTo updateAudioRequest.duration,
                    AudioResponse::uploadedAt setTo updateAudioRequest.uploadedAt
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

    override suspend fun getFeaturedAudio(): FeaturedAudioResponse {
        return db.getCollection<FeaturedAudioResponse>("featured_audio")
            .findOne("{displayedAt: {${MongoOperator.regex}: '${convertTime(System.currentTimeMillis())}.*'}}")
            ?: throw NoSuchElementException("Audio doesn't exists")
    }

    override suspend fun setFeaturedAudio(): FeaturedAudioResponse {
        var done: Boolean
        var featuredAudio: FeaturedAudioResponse?

        do {
            val randomAudio = db.getCollection<AudioResponse>("audio")
                .aggregate<AudioResponse>("[{ ${MongoOperator.sample}: { size: 1 } }]")
                .first() ?: throw NoSuchElementException("Audio doesn't exists")

            featuredAudio = FeaturedAudioResponse(
                mediaId = randomAudio.mediaId,
                title = randomAudio.title,
                album = randomAudio.album,
                artist = randomAudio.artist,
                imageUrl = randomAudio.imageUrl,
                songUrl = randomAudio.songUrl,
                description = randomAudio.description,
                synopsis = randomAudio.synopsis,
                duration = randomAudio.duration,
                uploadedAt = randomAudio.uploadedAt,
                displayedAt = convertTimeWithHour(System.currentTimeMillis())
            )

            done = db.getCollection<FeaturedAudioResponse>("featured_audio")
                .insertOne(featuredAudio)
                .wasAcknowledged()
        } while (!done)
        return featuredAudio ?: throw OperationsException("setFeaturedAudio encounter a problem!")
    }

    private fun convertTimeWithHour(time: Long): String {
        val timezoneIndonesia = time + 25_200_000
        val date = Date(timezoneIndonesia)
        val format: Format = SimpleDateFormat("dd MMMM yyyy HH:mm:ss")
        return format.format(date)
    }

    private fun convertTime(time: Long): String {
        val timezoneIndonesia = time + 25_200_000
        val date = Date(timezoneIndonesia)
        val format: Format = SimpleDateFormat("dd MMMM yyyy")
        return format.format(date)
    }
}