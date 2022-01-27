package com.injilkeselamatan.audio.data

import com.injilkeselamatan.audio.data.models.AudioResponse
import com.injilkeselamatan.audio.data.models.CreateAudioRequest
import com.injilkeselamatan.audio.data.models.FeaturedAudioResponse
import com.injilkeselamatan.audio.data.models.UpdateAudioRequest

interface AudioRepository {

    suspend fun getAllAudio(): List<AudioResponse>

    suspend fun getAudio(mediaId: String): AudioResponse

    suspend fun createAudio(createAudioRequest: CreateAudioRequest): AudioResponse

    suspend fun updateAudio(mediaId: String, updateAudioRequest: UpdateAudioRequest): AudioResponse

    suspend fun deleteAudio(mediaId: String)

    suspend fun getFeaturedAudio(): FeaturedAudioResponse

    suspend fun setFeaturedAudio(): FeaturedAudioResponse
}