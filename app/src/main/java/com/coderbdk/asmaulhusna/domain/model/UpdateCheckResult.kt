package com.coderbdk.asmaulhusna.domain.model

import com.coderbdk.asmaulhusna.data.remote.model.AppConfigResponse
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
sealed interface UpdateCheckResult {
    @Serializable
    data object NoUpdateNeeded : UpdateCheckResult

    @Serializable
    data class ForceAppUpdate(val remoteVersion: Int) : UpdateCheckResult

    @Serializable
    data class DataUpdateNeeded(
        val modules: List<DataModule>,
        val remoteConfig: AppConfigResponse,
        val isMandatory: Boolean
    ) : UpdateCheckResult
}

fun UpdateCheckResult.encodeToString(): String {
    return Json.encodeToString(UpdateCheckResult.serializer(), this)
}

fun String.decodeToUpdateCheckResult(): UpdateCheckResult {
    return Json.decodeFromString(UpdateCheckResult.serializer(), this)
}