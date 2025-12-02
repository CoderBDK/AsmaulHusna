package com.coderbdk.asmaulhusna.domain.usecase

import com.coderbdk.asmaulhusna.data.repository.SettingsRepository
import javax.inject.Inject

class SetFirstTimeUserUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(isFirstTime: Boolean) {
        repository.setFirstTimeUser(isFirstTime)
    }
}