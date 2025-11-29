package com.coderbdk.asmaulhusna.domain.usecase

import com.coderbdk.asmaulhusna.BuildConfig
import com.coderbdk.asmaulhusna.data.remote.model.Resource
import com.coderbdk.asmaulhusna.data.repository.SettingsRepository
import com.coderbdk.asmaulhusna.data.repository.UpdateRepository
import com.coderbdk.asmaulhusna.domain.model.DataModule
import com.coderbdk.asmaulhusna.domain.model.UpdateCheckResult
import kotlinx.coroutines.flow.first
import javax.inject.Inject


class CheckAppUpdateUseCase @Inject constructor(
    private val updateRepo: UpdateRepository,
    private val settingsRepo: SettingsRepository
) {
    suspend operator fun invoke(): UpdateCheckResult {
        val configResult = updateRepo.fetchAppConfig()

        if (configResult is Resource.Error) {
            return UpdateCheckResult.NoUpdateNeeded
        }
        val remoteConfig = (configResult as Resource.Success).data

        val localSettings = settingsRepo.settingsFlow.first()

        val localAppVersion = BuildConfig.VERSION_CODE
        if (localAppVersion < remoteConfig.minAppVersionCode) {
            return UpdateCheckResult.ForceAppUpdate(remoteConfig.minAppVersionCode)
        }

        val requiredUpdates = mutableListOf<DataModule>()

        // Language
        val remoteLangVersion = remoteConfig.dataModules["language"] ?: 0
        if (localSettings.languageVersion < remoteLangVersion) {
            requiredUpdates.add(DataModule.LANGUAGE)
        }

        // Asmaul Husna
        val remoteAsmaVersion = remoteConfig.dataModules["asmaul_husna"] ?: 0
        if (localSettings.asmaulHusnaVersion < remoteAsmaVersion) {
            requiredUpdates.add(DataModule.ASMAUL_HUSNA)
        }

        // Asmaul Husna Details
        val remoteAsmaDetailsVersion = remoteConfig.dataModules["asmaul_husna_details"] ?: 0
        if (localSettings.asmaulHusnaDetailsVersion < remoteAsmaDetailsVersion) {
            requiredUpdates.add(DataModule.ASMAUL_HUSNA_DETAILS)
        }

        return if (requiredUpdates.isNotEmpty()) {
            UpdateCheckResult.DataUpdateNeeded(
                modules = requiredUpdates,
                remoteConfig = remoteConfig,
                isMandatory = remoteConfig.isDataUpdateMandatory
            )
        } else {
            UpdateCheckResult.NoUpdateNeeded
        }
    }
}