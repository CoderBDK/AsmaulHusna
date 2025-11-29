package com.coderbdk.asmaulhusna.di

import com.coderbdk.asmaulhusna.data.local.db.dao.AsmaulHusnaDao
import com.coderbdk.asmaulhusna.data.local.db.dao.AsmaulHusnaDetailsDao
import com.coderbdk.asmaulhusna.data.local.db.dao.LanguageDao
import com.coderbdk.asmaulhusna.data.local.prefs.SettingsPreferences
import com.coderbdk.asmaulhusna.data.remote.ApiService
import com.coderbdk.asmaulhusna.data.repository.AsmaulHusnaRepository
import com.coderbdk.asmaulhusna.data.repository.AsmaulHusnaRepositoryImpl
import com.coderbdk.asmaulhusna.data.repository.LanguageRepository
import com.coderbdk.asmaulhusna.data.repository.LanguageRepositoryImpl
import com.coderbdk.asmaulhusna.data.repository.SettingsRepository
import com.coderbdk.asmaulhusna.data.repository.SettingsRepositoryImpl
import com.coderbdk.asmaulhusna.data.repository.UpdateRepository
import com.coderbdk.asmaulhusna.data.repository.UpdateRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideAsmaulHusnaRepository(
        asmaulHusnaDao: AsmaulHusnaDao,
        asmaulHusnaDetailsDao: AsmaulHusnaDetailsDao,
        apiService: ApiService

    ): AsmaulHusnaRepository {
        return AsmaulHusnaRepositoryImpl(asmaulHusnaDao, asmaulHusnaDetailsDao, apiService)
    }

    @Singleton
    @Provides
    fun provideLanguageRepository(
        languageDao: LanguageDao,
        apiService: ApiService,
    ): LanguageRepository {
        return LanguageRepositoryImpl(languageDao, apiService)
    }

    @Singleton
    @Provides
    fun provideSettingsRepository(
        prefs: SettingsPreferences
    ): SettingsRepository {
        return SettingsRepositoryImpl(prefs)
    }

    @Singleton
    @Provides
    fun provideUpdateRepository(
        apiService: ApiService,
    ): UpdateRepository {
        return UpdateRepositoryImpl(apiService)
    }
}