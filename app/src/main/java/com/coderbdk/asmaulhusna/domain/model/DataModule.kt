package com.coderbdk.asmaulhusna.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class DataModule(val moduleName: String) {
    LANGUAGE("language"),
    ASMAUL_HUSNA("asmaul_husna"),
    ASMAUL_HUSNA_DETAILS("asmaul_husna_details");

    fun getRemoteKey() = moduleName
}