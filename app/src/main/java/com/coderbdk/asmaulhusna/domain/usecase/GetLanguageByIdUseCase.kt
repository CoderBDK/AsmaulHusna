package com.coderbdk.asmaulhusna.domain.usecase

import com.coderbdk.asmaulhusna.data.local.db.entity.LanguageEntity
import com.coderbdk.asmaulhusna.data.repository.LanguageRepository
import javax.inject.Inject

class GetLanguageByIdUseCase @Inject constructor(
    private val repository: LanguageRepository
) {
    suspend operator fun invoke(id: Int): LanguageEntity? {
        return repository.getLanguageById(id)
    }
}