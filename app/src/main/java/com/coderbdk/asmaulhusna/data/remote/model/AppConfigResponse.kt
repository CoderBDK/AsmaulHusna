package com.coderbdk.asmaulhusna.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AppConfigResponse(
    val minAppVersionCode: Int,
    val isAppUpdateMandatory: Boolean,
    val isDataUpdateMandatory: Boolean,
    val dataModules: Map<String, Int>
)