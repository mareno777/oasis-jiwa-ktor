package com.injilkeselamatan.version.data

import com.injilkeselamatan.version.data.models.VersionResponse

interface VersionRepository {

    suspend fun getVersionCode(): VersionResponse
}