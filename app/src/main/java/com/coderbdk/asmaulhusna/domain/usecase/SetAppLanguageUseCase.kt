package com.coderbdk.asmaulhusna.domain.usecase

import com.coderbdk.asmaulhusna.data.repository.SettingsRepository
import javax.inject.Inject

class SetAppLanguageUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(langId: Int) {
        repository.selectLanguageId(langId)
    }
}