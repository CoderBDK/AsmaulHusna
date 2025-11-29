package com.coderbdk.asmaulhusna.domain.usecase

import com.coderbdk.asmaulhusna.data.remote.model.Resource
import com.coderbdk.asmaulhusna.data.repository.AsmaulHusnaRepository
import com.coderbdk.asmaulhusna.data.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class InitializeAsmaulHusnaDataUseCase @Inject constructor(
    private val asmaulHusnaRepo: AsmaulHusnaRepository,
    private val settingsRepo: SettingsRepository
) {
    suspend operator fun invoke(): Resource<String>? {
        val currentSettings = settingsRepo.settingsFlow.first()
        val currentLanguageId = currentSettings.selectedLanguageId

        return asmaulHusnaRepo.initAsmaulHusnaWithDetails(currentLanguageId)
    }
}