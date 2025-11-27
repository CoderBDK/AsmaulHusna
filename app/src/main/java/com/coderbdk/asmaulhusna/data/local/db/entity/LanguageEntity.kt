package com.coderbdk.asmaulhusna.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "languages")
data class LanguageEntity(
    @PrimaryKey
    val languageId: Int,
    val languageCode: String,
    val languageName: String,
    val languageCountry: String
)
