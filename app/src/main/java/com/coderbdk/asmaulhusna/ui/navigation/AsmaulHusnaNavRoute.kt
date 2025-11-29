package com.coderbdk.asmaulhusna.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AsmaulHusnaNavRoute {
    @Serializable
    data object Splash : AsmaulHusnaNavRoute()

    @Serializable
    object Onboarding : AsmaulHusnaNavRoute()

    @Serializable
    data object Home : AsmaulHusnaNavRoute()

    @Serializable
    data class Details(val number: Int) : AsmaulHusnaNavRoute()

    @Serializable
    data object Favorite : AsmaulHusnaNavRoute()

    @Serializable
    data object Settings : AsmaulHusnaNavRoute()

    @Serializable
    data class Update(val data: String? = null) : AsmaulHusnaNavRoute()
}
