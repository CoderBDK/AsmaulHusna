package com.coderbdk.asmaulhusna.domain.usecase

import com.coderbdk.asmaulhusna.data.local.db.entity.LanguageEntity
import com.coderbdk.asmaulhusna.data.repository.LanguageRepository
import javax.inject.Inject

class GetLanguageListUseCase @Inject constructor(
    private val repository: LanguageRepository
) {
    suspend operator fun invoke(): List<LanguageEntity> {
        return repository.getAllLanguage()
    }
}