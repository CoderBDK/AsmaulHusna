package com.coderbdk.asmaulhusna.data.repository

import com.coderbdk.asmaulhusna.data.local.db.dao.LanguageDao
import com.coderbdk.asmaulhusna.data.local.db.entity.LanguageEntity
import com.coderbdk.asmaulhusna.data.remote.ApiService
import com.coderbdk.asmaulhusna.data.remote.model.LanguageResponse
import com.coderbdk.asmaulhusna.data.remote.model.Resource
import com.coderbdk.asmaulhusna.data.util.mapper.toEntity
import javax.inject.Inject

class LanguageRepositoryImpl @Inject constructor(
    private val languageDao: LanguageDao,
    private val apiService: ApiService
) : LanguageRepository {

    override suspend fun getAllLanguage(): List<LanguageEntity> {
        return languageDao.getAll()
    }

    override suspend fun getLanguageById(id: Int): LanguageEntity? {
        return languageDao.getById(id)
    }

    override suspend fun initLanguage(): Resource<String>? {
        if (getAllLanguage().isNotEmpty()) return null
        return fetchAndInsertAllLanguage()
    }

    override suspend fun updateLanguageData(): Resource<String> {
       return fetchAndInsertAllLanguage()
    }

    private suspend fun fetchAndInsertAllLanguage(): Resource<String> {
        val result = apiService.get<List<LanguageResponse>>("api/?f=language")
        return when (result) {
            is Resource.Error -> {
                Resource.Error(result.message)
            }
            Resource.Loading -> {
                Resource.Loading
            }

            is Resource.Success -> {
                val entities = result.data.map { it.toEntity() }
                languageDao.upsertAll(entities)
                Resource.Success("OK")
            }
        }
    }

}