package com.coderbdk.asmaulhusna.data.model

data class SettingsModel(
    val selectedLanguageId: Int,
    val isFirstTimeUser: Boolean,
    val isDarkMode: Boolean,
    val languageVersion: Int,
    val asmaulHusnaVersion: Int,
    val asmaulHusnaDetailsVersion: Int
)
