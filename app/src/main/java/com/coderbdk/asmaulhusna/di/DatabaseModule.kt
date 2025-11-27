package com.coderbdk.asmaulhusna.di

import android.content.Context
import androidx.room.Room
import com.coderbdk.asmaulhusna.data.local.db.AsmaulHusnaDatabase
import com.coderbdk.asmaulhusna.data.local.db.dao.AsmaulHusnaDao
import com.coderbdk.asmaulhusna.data.local.db.dao.AsmaulHusnaDetailsDao
import com.coderbdk.asmaulhusna.data.local.db.dao.LanguageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AsmaulHusnaDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AsmaulHusnaDatabase::class.java,
            "asmaul_husna_database"
        ).build()
    }

    @Provides
    fun provideAsmaulHusnaDao(database: AsmaulHusnaDatabase): AsmaulHusnaDao {
        return database.asmaulHusnaDao()
    }

    @Provides
    fun provideAsmaulHusnaDetailsDao(database: AsmaulHusnaDatabase): AsmaulHusnaDetailsDao {
        return database.asmaulHusnaDetailsDao()
    }

    @Provides
    fun provideLanguageDao(database: AsmaulHusnaDatabase): LanguageDao {
        return database.languageDao()
    }

}