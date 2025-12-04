package com.coderbdk.asmaulhusna.ui.splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coderbdk.asmaulhusna.R
import com.coderbdk.asmaulhusna.ui.components.AnimatedNonagon
import com.coderbdk.asmaulhusna.ui.theme.AsmaulHusnaTheme
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SplashScreen(uiState: SplashUiState) {

    val infiniteTransition = rememberInfiniteTransition()


    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearOutSlowInEasing),
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
                    .size(240.dp)
                    .padding(16.dp),
                strokeWidth = 9f
            ) {
                Text(
                    text = stringResource(R.string.allah),
                    fontSize = 48.sp,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        color = colorScheme.primary,
                        shadow = Shadow(
                            color = colorScheme.outlineVariant,
                            blurRadius = 10f - phase,
                            offset = Offset(2f, 1f)
                        )
                    ),
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