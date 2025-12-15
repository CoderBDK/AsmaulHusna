package com.coderbdk.asmaulhusna.ui.splash

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.coderbdk.asmaulhusna.R
import com.coderbdk.asmaulhusna.ui.components.AnimatedNonagon
import com.coderbdk.asmaulhusna.ui.theme.AsmaulHusnaTheme


@Composable
fun SplashScreen(uiState: SplashUiState) {

    val infiniteTransition = rememberInfiniteTransition()


    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            NonagonLoading(
                modifier = Modifier
                    .size(300.dp)
                    .padding(16.dp),
                strokeWidth = 9f
            ) {
                Image(
                    painter = painterResource(R.drawable.asmaul_husna_splash),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(colorScheme.primary),
                    alpha = alpha,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(164.dp)
                )
            }
        }
    }
}

@Composable
fun NonagonLoading(
    modifier: Modifier = Modifier,
    strokeWidth: Float = 4f,
    content: @Composable (BoxScope.() -> Unit)
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        AnimatedNonagon(
            modifier = Modifier
                .fillMaxSize()
                .size(200.dp)
                .padding(16.dp),
            strokeWidth = strokeWidth
        )
        content(this)
    }

}


@Preview(showBackground = true)
@Composable
private fun SplashPreview() {
    AsmaulHusnaTheme {
        SplashScreen(
            uiState = SplashUiState(isLoading = true)
        )
    }
}