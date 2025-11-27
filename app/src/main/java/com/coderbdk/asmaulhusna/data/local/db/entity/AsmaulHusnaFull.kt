package com.coderbdk.asmaulhusna.data.local.db.entity


data class AsmaulHusnaFull(
    val number: Int,
    val arabicName: String,
    val audioFile: String,
    val isFavorite: Boolean,
    val transliteration: String,
    val meaning: String,
    val description: String,
    val languageName: String
)
