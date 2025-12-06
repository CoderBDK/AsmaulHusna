package com.coderbdk.asmaulhusna.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

@Composable
fun AsmaulHusnaApp(modifier: Modifier = Modifier) {
    val controller = rememberNavController()
    AsmaulHusnaNavGraph(controller = controller, modifier = Modifier)
}