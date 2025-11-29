package com.coderbdk.asmaulhusna.data.repository

import com.coderbdk.asmaulhusna.data.model.SettingsModel
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settingsFlow: Flow<SettingsModel>
    suspend fun selectLanguageId(langId: Int)
    suspend fun setFirstTimeUser(isFirstTime: Boolean)
    suspend fun toggleDarkMode(isDark: Boolean)
    suspend fun updateLocalVersion(
        languageVersion: Int? = null,
        asmaulHusnaVersion: Int? = null,
        detailsVersion: Int? = null
    )
}