package com.coderbdk.asmaulhusna.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AsmaulHusnaDetailsResponse(
    val number: Int,
    val languageId: Int,
    val transliteration: String,
    val meaning: String,
    val description: String
)