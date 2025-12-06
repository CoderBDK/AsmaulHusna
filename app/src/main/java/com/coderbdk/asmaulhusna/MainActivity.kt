package com.coderbdk.asmaulhusna

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coderbdk.asmaulhusna.ui.navigation.AsmaulHusnaApp
import com.coderbdk.asmaulhusna.ui.settings.SettingsViewModel
import com.coderbdk.asmaulhusna.ui.theme.AsmaulHusnaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsState by settingsViewModel.uiState.collectAsStateWithLifecycle()
                AsmaulHusnaTheme(
                    darkTheme = settingsState.isDarkTheme
                ) {
                    Surface {
                        AsmaulHusnaApp()
                    }
                }

        }
    }
}
