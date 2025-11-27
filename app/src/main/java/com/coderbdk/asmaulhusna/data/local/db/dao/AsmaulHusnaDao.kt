package com.coderbdk.asmaulhusna.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.coderbdk.asmaulhusna.data.local.db.entity.AsmaulHusnaEntity
import com.coderbdk.asmaulhusna.data.local.db.entity.AsmaulHusnaFull
import kotlinx.coroutines.flow.Flow

@Dao
interface AsmaulHusnaDao {

    @Query("UPDATE asmaul_husna SET isFavorite = :favorite WHERE number = :number")
    suspend fun setFavorite(number: Int, favorite: Boolean)

    @Upsert
    suspend fun upsertAllAsmaulHusna(asmas: List<AsmaulHusnaEntity>)

    @Transaction
    @Query("""
    SELECT 
        A.number, 
        A.arabicName, 
        A.audioFile, 
        A.isFavorite,
        D.transliteration,
        D.meaning,
        D.description,
        L.languageName
    FROM asmaul_husna AS A
    INNER JOIN asmaul_husna_details AS D 
        ON A.number = D.number 
    INNER JOIN languages AS L 
        ON D.languageId = L.languageId
    WHERE D.languageId = :languageId
    ORDER BY A.number ASC
""")
    fun getAllWithLanguage(languageId: Int): Flow<List<AsmaulHusnaFull>>

    @Query("SELECT * FROM asmaul_husna WHERE isFavorite = 1 ORDER BY number")
    fun getAllFavorites(): Flow<List<AsmaulHusnaEntity>>

    @Query("""
    SELECT COUNT(A.number)
    FROM asmaul_husna AS A
    INNER JOIN asmaul_husna_details AS D 
        ON A.number = D.number 
    WHERE D.languageId = :languageId
""")
    suspend fun countAsmaulHusnaByLanguage(languageId: Int): Int
}
