package com.coderbdk.asmaulhusna.data.repository

import com.coderbdk.asmaulhusna.data.local.db.entity.AsmaulHusnaEntity
import com.coderbdk.asmaulhusna.data.local.db.entity.AsmaulHusnaFull
import com.coderbdk.asmaulhusna.data.remote.model.Resource
import kotlinx.coroutines.flow.Flow

interface AsmaulHusnaRepository {

    fun getAllAsmaulHusnaWithLanguage(currentLanguageId: Int): Flow<List<AsmaulHusnaFull>>
    fun getAllFavoritesAsmaulHusna(): Flow<List<AsmaulHusnaEntity>>

    suspend fun setFavorite(number: Int, favorite: Boolean)
    suspend fun initAsmaulHusnaWithDetails(currentLanguageId: Int): Resource<String>?
    suspend fun updateAsmaulHusnaData(): Resource<String>
    suspend fun updateAsmaulHusnaDetails(): Resource<String>
}