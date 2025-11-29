package com.coderbdk.asmaulhusna.data.local.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.coderbdk.asmaulhusna.data.model.SettingsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.io.IOException

private const val SETTINGS_PREFERENCES_NAME = "settings_preferences"
private const val DEFAULT_SELECTED_LANGUAGE_ID = 15
private const val DEFAULT_LANGUAGE_VERSION = 1
private const val DEFAULT_ASMAUL_HUSNA_VERSION = 1
private const val DEFAULT_ASMAUL_HUSNA_DETAILS_VERSION = 1
private val Context.dataStore by preferencesDataStore(
    name = SETTINGS_PREFERENCES_NAME
)

private object PreferencesKeys {
    val SELECTED_LANGUAGE_ID = intPreferencesKey("selected_language_id")
    val IS_FIRST_TIME_USER = booleanPreferencesKey("is_first_time_user")
    val LANGUAGE_VERSION = intPreferencesKey("language_version")
    val ASMAUL_HUSNA_VERSION = intPreferencesKey("asmaul_husna_version")
    val ASMAUL_HUSNA_DETAILS_VERSION = intPreferencesKey("asmaul_husna_details_version")
    val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
}

class SettingsPreferences(private val context: Context) {
    val settingsPreferencesFlow: Flow<SettingsModel> =
        context.dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val selectedLanguageId =
                preferences[PreferencesKeys.SELECTED_LANGUAGE_ID] ?: DEFAULT_SELECTED_LANGUAGE_ID
            val isFirstTimeUser = preferences[PreferencesKeys.IS_FIRST_TIME_USER] ?: true
            val isDarkMode = preferences[PreferencesKeys.IS_DARK_MODE] ?: false
            val languageVersion =
                preferences[PreferencesKeys.LANGUAGE_VERSION] ?: DEFAULT_LANGUAGE_VERSION
            val asmaulHusnaVersion =
                preferences[PreferencesKeys.ASMAUL_HUSNA_VERSION] ?: DEFAULT_ASMAUL_HUSNA_VERSION
            val asmaulHusnaDetailsVersion =
                preferences[PreferencesKeys.ASMAUL_HUSNA_DETAILS_VERSION]
                    ?: DEFAULT_ASMAUL_HUSNA_DETAILS_VERSION

            SettingsModel(
                selectedLanguageId = selectedLanguageId,
                isFirstTimeUser = isFirstTimeUser,
                isDarkMode = isDarkMode,
                languageVersion = languageVersion,
                asmaulHusnaVersion = asmaulHusnaVersion,
                asmaulHusnaDetailsVersion = asmaulHusnaDetailsVersion
            )
        }

    suspend fun selectLanguageId(langId: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_LANGUAGE_ID] = langId
        }
    }

    suspend fun setFirstTimeUser(isFirstTime: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_FIRST_TIME_USER] = isFirstTime
        }
    }

    suspend fun updateVersions(
        languageVersion: Int? = null,
        asmaulHusnaVersion: Int? = null,
        detailsVersion: Int? = null
    ) {
        context.dataStore.edit { preferences ->
            languageVersion?.let { preferences[PreferencesKeys.LANGUAGE_VERSION] = it }
            asmaulHusnaVersion?.let { preferences[PreferencesKeys.ASMAUL_HUSNA_VERSION] = it }
            detailsVersion?.let { preferences[PreferencesKeys.ASMAUL_HUSNA_DETAILS_VERSION] = it }
        }
    }
    suspend fun toggleDarkMode(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_DARK_MODE] = isDark
        }
    }
}