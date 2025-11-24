package com.coderbdk.asmaulhusna.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AsmaulHusnaNavRoute {
    @Serializable
    data object Splash : AsmaulHusnaNavRoute()

    @Serializable
    data object Home : AsmaulHusnaNavRoute()

    @Serializable
    data object Details : AsmaulHusnaNavRoute()

    @Serializable
    data object Favorite : AsmaulHusnaNavRoute()

    @Serializable
    data object Settings : AsmaulHusnaNavRoute()
}
