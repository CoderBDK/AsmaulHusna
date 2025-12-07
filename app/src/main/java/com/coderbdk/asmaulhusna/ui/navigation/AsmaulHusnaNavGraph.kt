package com.coderbdk.asmaulhusna.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.coderbdk.asmaulhusna.ui.details.DetailsRoute
import com.coderbdk.asmaulhusna.ui.favorite.FavoriteRoute
import com.coderbdk.asmaulhusna.ui.home.HomeRoute
import com.coderbdk.asmaulhusna.ui.onboarding.OnboardingRoute
import com.coderbdk.asmaulhusna.ui.onboarding.OnboardingScreen
import com.coderbdk.asmaulhusna.ui.settings.SettingsRoute
import com.coderbdk.asmaulhusna.ui.splash.SplashRoute
import com.coderbdk.asmaulhusna.ui.update.UpdateRoute

@Composable
fun AsmaulHusnaNavGraph(controller: NavHostController, modifier: Modifier = Modifier) {
    val navActions = AsmaulHusnaNavActions(controller)

    NavHost(
        navController = controller,
        startDestination = AsmaulHusnaNavRoute.Splash,
        modifier = modifier
    ) {
        composable<AsmaulHusnaNavRoute.Splash> {
            SplashRoute(
                onNavigateToHome = navActions::navigateToHome,
                onNavigateToOnboarding = navActions::navigateToOnboarding,
                onNavigateToUpdate = navActions::navigateToUpdate
            )
        }
        composable<AsmaulHusnaNavRoute.Onboarding> {
            OnboardingRoute(onNavigateToHome = navActions::navigateToHome)
        }

        composable<AsmaulHusnaNavRoute.Home> {
            HomeRoute(
                onNavigateToFavorite = navActions::navigateToFavorite,
                onNavigateToDetails = navActions::navigateToDetails,
                onNavigateToSettings = navActions::navigateToSettings
            )
        }
        composable<AsmaulHusnaNavRoute.Details> {
            DetailsRoute(onNavigateUp = navActions::navigateUp)
        }
        composable<AsmaulHusnaNavRoute.Favorite> {
            FavoriteRoute(onNavigateUp = navActions::navigateUp, onNavigateToDetails = navActions::navigateToDetails)
        }
        composable<AsmaulHusnaNavRoute.Settings> {
            SettingsRoute(onNavigateUp = navActions::navigateUp)
        }
        composable<AsmaulHusnaNavRoute.Update> {
            UpdateRoute(onNavigateToHome = navActions::navigateToHome)
        }
    }
}