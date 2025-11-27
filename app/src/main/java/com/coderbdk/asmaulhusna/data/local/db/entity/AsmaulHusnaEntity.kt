package com.coderbdk.asmaulhusna.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("asmaul_husna")
data class AsmaulHusnaEntity(
    @PrimaryKey
    val number: Int,
    val arabicName: String,
    val audioFile: String,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)