package com.coderbdk.asmaulhusna.data.repository

import com.coderbdk.asmaulhusna.data.remote.ApiService
import com.coderbdk.asmaulhusna.data.remote.model.AppConfigResponse
import com.coderbdk.asmaulhusna.data.remote.model.Resource
import javax.inject.Inject

class UpdateRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : UpdateRepository {

    override suspend fun fetchAppConfig(): Resource<AppConfigResponse> {
        return apiService.get<AppConfigResponse>("api/?f=config")
    }
}