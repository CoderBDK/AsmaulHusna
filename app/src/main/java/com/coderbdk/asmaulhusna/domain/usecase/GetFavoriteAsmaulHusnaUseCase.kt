package com.coderbdk.asmaulhusna.domain.usecase

import com.coderbdk.asmaulhusna.data.local.db.entity.AsmaulHusnaEntity
import com.coderbdk.asmaulhusna.data.repository.AsmaulHusnaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteAsmaulHusnaUseCase @Inject constructor(
    private val repository: AsmaulHusnaRepository
) {
    operator fun invoke(): Flow<List<AsmaulHusnaEntity>> {
        return repository.getAllFavoritesAsmaulHusna()
    }
}