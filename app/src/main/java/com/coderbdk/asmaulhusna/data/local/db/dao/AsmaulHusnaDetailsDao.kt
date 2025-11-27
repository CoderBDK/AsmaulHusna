package com.coderbdk.asmaulhusna.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Upsert
import com.coderbdk.asmaulhusna.data.local.db.entity.AsmaulHusnaDetailsEntity

@Dao
interface AsmaulHusnaDetailsDao {
    @Upsert
    suspend fun upsertAll(details: List<AsmaulHusnaDetailsEntity>)
}