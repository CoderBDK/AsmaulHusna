package com.coderbdk.asmaulhusna.data.repository

import com.coderbdk.asmaulhusna.data.local.prefs.SettingsPreferences
import com.coderbdk.asmaulhusna.data.model.SettingsModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(private val prefs: SettingsPreferences) :
    SettingsRepository {

    override val settingsFlow: Flow<SettingsModel> = prefs.settingsPreferencesFlow

    override suspend fun selectLanguageId(langId: Int) {
        prefs.selectLanguageId(langId)
    }

    override suspend fun setFirstTimeUser(isFirstTime: Boolean) {
        prefs.setFirstTimeUser(isFirstTime)
    }

    override suspend fun toggleDarkMode(isDark: Boolean) {
        prefs.toggleDarkMode(isDark)
    }

    override suspend fun updateLocalVersion(
        languageVersion: Int?,
        asmaulHusnaVersion: Int?,
        detailsVersion: Int?
    ) {
        prefs.updateVersions(languageVersion, asmaulHusnaVersion, detailsVersion)
    }
}