package com.coderbdk.asmaulhusna.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AsmaulHusnaResponse(
    val number: Int,
    val arabicName: String,
    val audioFile: String,
)