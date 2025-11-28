package com.coderbdk.asmaulhusna.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class LanguageResponse(
    val languageId: Int,
    val languageCode: String,
    val languageName: String,
    val languageCountry: String
)