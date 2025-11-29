package com.coderbdk.asmaulhusna.data.repository

import com.coderbdk.asmaulhusna.data.remote.model.AppConfigResponse
import com.coderbdk.asmaulhusna.data.remote.model.Resource

interface UpdateRepository {
    suspend fun fetchAppConfig(): Resource<AppConfigResponse>
}