package com.coderbdk.asmaulhusna

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.coderbdk.asmaulhusna.ui.navigation.AsmaulHusnaApp
import com.coderbdk.asmaulhusna.ui.theme.AsmaulHusnaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AsmaulHusnaTheme {
                AsmaulHusnaApp()
            }
        }
    }
}
