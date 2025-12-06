package com.coderbdk.asmaulhusna.ui.navigation

import androidx.navigation.NavController
import com.coderbdk.asmaulhusna.domain.model.UpdateCheckResult
import com.coderbdk.asmaulhusna.domain.model.encodeToString

class AsmaulHusnaNavActions(private val controller: NavController) {

    fun navigateUp() = controller.navigateUp()

    fun navigateToUpdate(updateResult: UpdateCheckResult) {
        controller.navigate(AsmaulHusnaNavRoute.Update(updateResult.encodeToString())) {
            popUpTo(AsmaulHusnaNavRoute.Splash) { inclusive = true }
        }
    }

    fun navigateToOnboarding() {
        controller.navigate(AsmaulHusnaNavRoute.Onboarding) {
            popUpTo(AsmaulHusnaNavRoute.Splash) { inclusive = true }
        }
    }

    fun navigateToHome(routeToPopUpFrom: AsmaulHusnaNavRoute) {
        controller.navigate(AsmaulHusnaNavRoute.Home) {
            popUpTo(routeToPopUpFrom) { inclusive = true }
        }
    }
    fun navigateToDetails(number: Int) {
        controller.navigate(AsmaulHusnaNavRoute.Details(number))
    }
    fun navigateToSettings() {
        controller.navigate(AsmaulHusnaNavRoute.Settings)
    }
}