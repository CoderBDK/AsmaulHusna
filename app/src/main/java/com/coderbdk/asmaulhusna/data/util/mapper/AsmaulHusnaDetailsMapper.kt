package com.coderbdk.asmaulhusna.data.util.mapper

import com.coderbdk.asmaulhusna.data.local.db.entity.AsmaulHusnaDetailsEntity
import com.coderbdk.asmaulhusna.data.remote.model.AsmaulHusnaDetailsResponse

fun AsmaulHusnaDetailsResponse.toEntity(): AsmaulHusnaDetailsEntity {
    return AsmaulHusnaDetailsEntity(
        number = this.number,
        languageId = this.languageId,
        transliteration = this.transliteration,
        meaning = this.meaning,
        description = this.description
    )
}