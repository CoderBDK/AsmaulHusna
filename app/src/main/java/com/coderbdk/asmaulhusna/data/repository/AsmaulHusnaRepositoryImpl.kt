package com.coderbdk.asmaulhusna.data.repository

import com.coderbdk.asmaulhusna.data.local.db.dao.AsmaulHusnaDao
import com.coderbdk.asmaulhusna.data.local.db.dao.AsmaulHusnaDetailsDao
import com.coderbdk.asmaulhusna.data.local.db.entity.AsmaulHusnaDetailsEntity
import com.coderbdk.asmaulhusna.data.local.db.entity.AsmaulHusnaEntity
import com.coderbdk.asmaulhusna.data.local.db.entity.AsmaulHusnaFull
import com.coderbdk.asmaulhusna.data.remote.ApiService
import com.coderbdk.asmaulhusna.data.remote.model.AsmaulHusnaDetailsResponse
import com.coderbdk.asmaulhusna.data.remote.model.AsmaulHusnaResponse
import com.coderbdk.asmaulhusna.data.remote.model.Resource
import com.coderbdk.asmaulhusna.data.util.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AsmaulHusnaRepositoryImpl @Inject constructor(
    private val asmaulHusnaDao: AsmaulHusnaDao,
    private val asmaulHusnaDetailsDao: AsmaulHusnaDetailsDao,
    private val apiService: ApiService,
) : AsmaulHusnaRepository {

    override fun getAllAsmaulHusnaWithLanguage(currentLanguageId: Int): Flow<List<AsmaulHusnaFull>> {
        return asmaulHusnaDao.getAllWithLanguage(currentLanguageId)
    }

    override fun getAllFavoritesAsmaulHusna(): Flow<List<AsmaulHusnaEntity>> {
        return asmaulHusnaDao.getAllFavorites()
    }

    override suspend fun setFavorite(number: Int, favorite: Boolean) {
        asmaulHusnaDao.setFavorite(number, favorite)
    }

    override suspend fun initAsmaulHusnaWithDetails(currentLanguageId: Int): Resource<String>? {
        if (isAsmaulHusnaAlreadyAvailable(currentLanguageId)) return null
        val result = fetchAndInsertAsmaulHusna()
        if (result is Resource.Error) {
            return null
        }
        return fetchAndInsertAsmaulHusnaDetails()
    }

    override suspend fun updateAsmaulHusnaData(): Resource<String> {
        return fetchAndInsertAsmaulHusna()
    }

    override suspend fun updateAsmaulHusnaDetails(): Resource<String> {
        return fetchAndInsertAsmaulHusnaDetails()
    }

    private suspend fun fetchAndInsertAsmaulHusna(): Resource<String> {
        val result = apiService.get<List<AsmaulHusnaResponse>>("api/?f=asmaul_husna")
        return when (result) {
            is Resource.Error -> {
                Resource.Error(result.message)
            }

            Resource.Loading -> {
                Resource.Loading
            }

            is Resource.Success -> {
                val entities = result.data.map { it.toEntity() }
                upsertAllAsmaulHusna(entities)
                Resource.Success("OK")
            }
        }
    }

    private suspend fun fetchAndInsertAsmaulHusnaDetails(): Resource<String> {
        val result = apiService.get<List<AsmaulHusnaDetailsResponse>>("api/?f=asmaul_husna_details")
        return when (result) {
            is Resource.Error -> {
                Resource.Error(result.message)
            }

            Resource.Loading -> {
                Resource.Loading
            }

            is Resource.Success -> {
                val entities = result.data.map { it.toEntity() }
                upsertAllDetailsAsmaulHusna(entities)
                Resource.Success("OK")
            }
        }
    }

    private suspend fun isAsmaulHusnaAlreadyAvailable(currentLanguageId: Int): Boolean {
        return asmaulHusnaDao.countAsmaulHusnaByLanguage(currentLanguageId) == 99
    }

    private suspend fun upsertAllAsmaulHusna(asmas: List<AsmaulHusnaEntity>) {
        asmaulHusnaDao.upsertAllAsmaulHusna(asmas)
    }

    private suspend fun upsertAllDetailsAsmaulHusna(details: List<AsmaulHusnaDetailsEntity>) {
        asmaulHusnaDetailsDao.upsertAll(details)
    }


}