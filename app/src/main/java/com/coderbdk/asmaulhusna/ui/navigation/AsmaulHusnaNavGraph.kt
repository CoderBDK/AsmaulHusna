package com.coderbdk.asmaulhusna.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AsmaulHusnaNavGraph(controller: NavHostController, modifier: Modifier = Modifier) {
    val navActions = AsmaulHusnaNavActions(controller)

    NavHost(
        navController = controller,
        startDestination = AsmaulHusnaNavRoute.Splash,
        modifier = modifier
    ) {
        composable<AsmaulHusnaNavRoute.Splash> {

        }
        composable<AsmaulHusnaNavRoute.Home> {

        }
        composable<AsmaulHusnaNavRoute.Details> {

        }
        composable<AsmaulHusnaNavRoute.Favorite> {

        }
        composable<AsmaulHusnaNavRoute.Settings> {

        }

    }
}