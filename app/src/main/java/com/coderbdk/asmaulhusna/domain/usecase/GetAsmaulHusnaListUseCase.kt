package com.coderbdk.asmaulhusna.domain.usecase

import com.coderbdk.asmaulhusna.data.local.db.entity.AsmaulHusnaFull
import com.coderbdk.asmaulhusna.data.repository.AsmaulHusnaRepository
import com.coderbdk.asmaulhusna.data.repository.SettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class GetAsmaulHusnaListUseCase @Inject constructor(
    private val asmaulHusnaRepo: AsmaulHusnaRepository,
    private val settingsRepo: SettingsRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<AsmaulHusnaFull>> {
        return settingsRepo.settingsFlow
            .flatMapLatest { settingsModel ->
                val languageId = settingsModel.selectedLanguageId
                asmaulHusnaRepo.getAllAsmaulHusnaWithLanguage(languageId)
            }
    }
}