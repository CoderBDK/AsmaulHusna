package com.coderbdk.asmaulhusna.domain.usecase

import com.coderbdk.asmaulhusna.data.remote.model.AppConfigResponse
import com.coderbdk.asmaulhusna.data.remote.model.Resource
import com.coderbdk.asmaulhusna.data.repository.AsmaulHusnaRepository
import com.coderbdk.asmaulhusna.data.repository.LanguageRepository
import com.coderbdk.asmaulhusna.data.repository.SettingsRepository
import com.coderbdk.asmaulhusna.domain.model.DataModule
import javax.inject.Inject


class UpdateAppDataUseCase @Inject constructor(
    private val asmaulHusnaRepo: AsmaulHusnaRepository,
    private val languageRepo: LanguageRepository,
    private val settingsRepo: SettingsRepository,
) {
    suspend operator fun invoke(
        modulesToUpdate: List<DataModule>,
        remoteConfig: AppConfigResponse
    ): Resource<String> {

        val newVersions = mutableMapOf<DataModule, Int>()
        val failedUpdates = mutableListOf<String>()

        for (module in modulesToUpdate) {
            val result = when (module) {
                DataModule.LANGUAGE -> languageRepo.updateLanguageData()
                DataModule.ASMAUL_HUSNA -> asmaulHusnaRepo.updateAsmaulHusnaData()
                DataModule.ASMAUL_HUSNA_DETAILS -> asmaulHusnaRepo.updateAsmaulHusnaDetails()
            }

            if (result is Resource.Success<*>) {
                newVersions[module] = remoteConfig.dataModules[module.getRemoteKey()] ?: 0
            } else {
                failedUpdates.add(module.moduleName)
            }
        }

        if (newVersions.isNotEmpty()) {
            settingsRepo.updateLocalVersion(
                languageVersion = newVersions[DataModule.LANGUAGE],
                asmaulHusnaVersion = newVersions[DataModule.ASMAUL_HUSNA],
                detailsVersion = newVersions[DataModule.ASMAUL_HUSNA_DETAILS]
            )
        }

        return if (failedUpdates.isEmpty()) {
            Resource.Success("Data updated successfully.")
        } else {
            Resource.Error("Failed to update modules: ${failedUpdates.joinToString()}")
        }
    }
}