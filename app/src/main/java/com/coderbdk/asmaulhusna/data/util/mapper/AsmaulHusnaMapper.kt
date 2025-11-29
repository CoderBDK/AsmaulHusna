package com.coderbdk.asmaulhusna.data.util.mapper

import com.coderbdk.asmaulhusna.data.local.db.entity.AsmaulHusnaEntity
import com.coderbdk.asmaulhusna.data.remote.model.AsmaulHusnaResponse

fun AsmaulHusnaResponse.toEntity(): AsmaulHusnaEntity {
    return AsmaulHusnaEntity(
        number = this.number,
        arabicName = this.arabicName,
        audioFile = this.audioFile,
        isFavorite = false,
    )
}