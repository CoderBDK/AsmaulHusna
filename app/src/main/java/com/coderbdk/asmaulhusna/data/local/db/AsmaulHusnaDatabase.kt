package com.coderbdk.asmaulhusna.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.coderbdk.asmaulhusna.data.local.db.dao.AsmaulHusnaDao
import com.coderbdk.asmaulhusna.data.local.db.dao.AsmaulHusnaDetailsDao
import com.coderbdk.asmaulhusna.data.local.db.dao.LanguageDao
import com.coderbdk.asmaulhusna.data.local.db.entity.AsmaulHusnaDetailsEntity
import com.coderbdk.asmaulhusna.data.local.db.entity.AsmaulHusnaEntity
import com.coderbdk.asmaulhusna.data.local.db.entity.LanguageEntity

@Database(
    entities = [
        AsmaulHusnaEntity::class,
        AsmaulHusnaDetailsEntity::class,
        LanguageEntity::class
    ],
    version = 1
)
abstract class AsmaulHusnaDatabase : RoomDatabase() {
    abstract fun asmaulHusnaDao(): AsmaulHusnaDao
    abstract fun asmaulHusnaDetailsDao(): AsmaulHusnaDetailsDao
    abstract fun languageDao(): LanguageDao
}