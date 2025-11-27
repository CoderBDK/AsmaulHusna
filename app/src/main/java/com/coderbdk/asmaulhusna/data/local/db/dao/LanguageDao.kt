package com.coderbdk.asmaulhusna.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.coderbdk.asmaulhusna.data.local.db.entity.LanguageEntity

@Dao
interface LanguageDao {
    @Upsert
    suspend fun upsertAll(languages: List<LanguageEntity>)

    @Query("SELECT * FROM languages ORDER BY languageName")
    suspend fun getAll(): List<LanguageEntity>

    @Query("SELECT * FROM languages WHERE languageId = :id")
    suspend fun getById(id: Int): LanguageEntity?
}