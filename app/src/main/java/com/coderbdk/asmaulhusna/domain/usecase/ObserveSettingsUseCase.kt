package com.coderbdk.asmaulhusna.domain.usecase

import com.coderbdk.asmaulhusna.data.model.SettingsModel
import com.coderbdk.asmaulhusna.data.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<SettingsModel> {
        return repository.settingsFlow
    }
}