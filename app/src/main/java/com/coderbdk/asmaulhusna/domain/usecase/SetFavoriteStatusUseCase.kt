package com.coderbdk.asmaulhusna.domain.usecase

import com.coderbdk.asmaulhusna.data.repository.AsmaulHusnaRepository
import javax.inject.Inject

class SetFavoriteStatusUseCase @Inject constructor(
    private val repository: AsmaulHusnaRepository
) {
    suspend operator fun invoke(number: Int, favorite: Boolean) {
        repository.setFavorite(number, favorite)
    }
}