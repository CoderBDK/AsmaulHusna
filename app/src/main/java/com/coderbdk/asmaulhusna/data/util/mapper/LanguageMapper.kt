package com.coderbdk.asmaulhusna.data.util.mapper

import com.coderbdk.asmaulhusna.data.local.db.entity.LanguageEntity
import com.coderbdk.asmaulhusna.data.remote.model.LanguageResponse

fun LanguageResponse.toEntity(): LanguageEntity {
    return LanguageEntity(
        languageId = this.languageId,
        languageCode = this.languageCode,
        languageName = this.languageName,
        languageCountry = this.languageCountry,
    )
}