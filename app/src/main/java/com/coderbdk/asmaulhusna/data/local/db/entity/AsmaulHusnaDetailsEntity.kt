package com.coderbdk.asmaulhusna.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "asmaul_husna_details",
    indices = [
        Index(value = ["number"]),
        Index(value = ["languageId"])
    ],
    primaryKeys = ["number", "languageId"],
    foreignKeys = [
        ForeignKey(
            entity = AsmaulHusnaEntity::class,
            parentColumns = ["number"],
            childColumns = ["number"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LanguageEntity::class,
            parentColumns = ["languageId"],
            childColumns = ["languageId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
)
data class AsmaulHusnaDetailsEntity(
    val number: Int,
    val languageId: Int,
    val transliteration: String,
    val meaning: String,
    val description: String
)
