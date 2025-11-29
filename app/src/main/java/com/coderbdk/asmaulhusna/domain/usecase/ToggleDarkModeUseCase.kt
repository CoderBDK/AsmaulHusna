package com.coderbdk.asmaulhusna.domain.usecase

import com.coderbdk.asmaulhusna.data.repository.SettingsRepository
import javax.inject.Inject

class ToggleDarkModeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(isDark: Boolean) {
        repository.toggleDarkMode(isDark)
    }
}