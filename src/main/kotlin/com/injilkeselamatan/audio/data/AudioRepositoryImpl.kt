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
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*

class AudioRepositoryImpl(private val db: CoroutineDatabase) : AudioRepository {
    override suspend fun getAllAudio(): List<AudioResponse> {
        return db.getCollection<AudioResponse>("audio")
            .find()
            .ascendingSort(AudioResponse::mediaId)
            .toList()
    }

    override suspend fun createAudio(createAudioRequest: CreateAudioRequest): AudioResponse {
        val assignedMediaId = getLatestMediaId(createAudioRequest.album) + 3
        val assignedCreateAudioRequest =
            createAudioRequest.copy(mediaId = "media_${String.format("%04d", assignedMediaId)}") // Zero Leading in 4 digits

        val existedAudio = db.getCollection<AudioResponse>("audio")
            .findOne(
                or(
                    AudioResponse::mediaId eq assignedCreateAudioRequest.mediaId,
                    AudioResponse::title eq assignedCreateAudioRequest.title,
                    AudioResponse::songUrl eq assignedCreateAudioRequest.songUrl
                )
            )
        if (existedAudio != null) throw ResourceAlreadyExists("Audio already exists!")

        val successful = db.getCollection<CreateAudioRequest>("audio")
            .insertOne(assignedCreateAudioRequest).wasAcknowledged()
        if (successful) return assignedCreateAudioRequest.toAudioResponse()
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
            .findOne(filter = FeaturedAudioResponse::displayedAt eq convertTime(System.currentTimeMillis()))
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
                displayedAt = convertTime(System.currentTimeMillis())
            )

            done = db.getCollection<FeaturedAudioResponse>("featured_audio")
                .insertOne(featuredAudio)
                .wasAcknowledged()
        } while (!done)
        return featuredAudio ?: throw OperationsException("setFeaturedAudio encounter a problem!")
    }

    private fun convertTime(time: Long): String {
        val date = Date(time)
        val format: DateFormat = SimpleDateFormat("dd MMMM yyyy")
        format.timeZone = TimeZone.getTimeZone(ZoneId.of("Asia/Jakarta"))
        return format.format(date)
    }

    private suspend fun getLatestMediaId(album: String): Int {
        return db.getCollection<AudioResponse>("audio")
            .find(filter = AudioResponse::album eq album)
            .toList()
            .last().mediaId.filter { it.isDigit() }.toIntOrNull()
            ?: throw NoSuchElementException("conversation media_id was failed")
    }
}