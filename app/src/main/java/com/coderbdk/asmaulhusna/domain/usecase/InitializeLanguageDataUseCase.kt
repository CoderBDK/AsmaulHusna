package com.coderbdk.asmaulhusna.domain.usecase

import com.coderbdk.asmaulhusna.data.remote.model.Resource
import com.coderbdk.asmaulhusna.data.repository.LanguageRepository
import javax.inject.Inject

class InitializeLanguageDataUseCase @Inject constructor(
    private val repository: LanguageRepository
) {
    suspend operator fun invoke(): Resource<String>? {
        return repository.initLanguage()
    }
}