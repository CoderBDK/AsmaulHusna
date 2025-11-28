package com.coderbdk.asmaulhusna.data.repository

import com.coderbdk.asmaulhusna.data.local.db.entity.LanguageEntity
import com.coderbdk.asmaulhusna.data.remote.model.Resource

interface LanguageRepository {
    suspend fun getAllLanguage(): List<LanguageEntity>
    suspend fun getLanguageById(id: Int): LanguageEntity?
    suspend fun initLanguage(): Resource<String>?
    suspend fun updateLanguageData(): Resource<String>
}