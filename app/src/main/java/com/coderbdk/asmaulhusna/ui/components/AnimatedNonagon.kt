package com.coderbdk.asmaulhusna.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun AnimatedNonagon(
    modifier: Modifier = Modifier,
    strokeWidth: Float = 6f,
    trailLength: Int = 49
) {
    val infiniteTransition = rememberInfiniteTransition()

    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val trail = remember { mutableStateListOf<Offset>() }

    val indicatorColor = colorScheme.primary
    val trackColor = colorScheme.secondaryContainer
    val borderColor = colorScheme.outlineVariant

    Canvas(modifier = modifier) {

        val radius = size.minDimension / 2.5f
        val center = Offset(size.width / 2, size.height / 2)

        val points = (0 until 9).map { i ->
            val angle = Math.toRadians((i * (360.0 / 9)) - 90)
            Offset(
                x = center.x + (radius * cos(angle)).toFloat(),
                y = center.y + (radius * sin(angle)).toFloat()
            )
        }

        // Draw Nonagon Border
        for (i in points.indices) {
            val next = (i + 1) % points.size
            drawLine(
                color = borderColor,
                start = points[i],
                end = points[next],
                strokeWidth = strokeWidth + 2,
            )
            drawLine(
                color = trackColor,
                start = points[i],
                end = points[next],
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round,
            )
        }


        // Calculate Animated Moving Point
        val totalEdges = points.size
        val position = phase * totalEdges
        val edgeIndex = position.toInt()
        val t = position - edgeIndex

        val start = points[edgeIndex % totalEdges]
        val end = points[(edgeIndex + 1) % totalEdges]

        val head = Offset(
            x = lerp(start.x, end.x, t),
            y = lerp(start.y, end.y, t)
        )

        // Add new head point to trail
        trail.add(0, head)
        if (trail.size > trailLength) trail.removeAt(trail.lastIndex)

        // TRAIL (fade effect)
        trail.forEachIndexed { i, p ->
            val alpha = (1f - (i / trailLength.toFloat()))
            if (i < trail.size - 1) {
                drawLine(
                    color = indicatorColor.copy(alpha),
                    start = p,
                    end = trail[i + 1],
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }
        }
        for (i in points.indices) {
            drawCircle(
                color = borderColor,
                center = points[i],
                radius = strokeWidth + 2f,
                style = Stroke(width = strokeWidth)
            )
            drawCircle(
                color = indicatorColor,
                center = points[i],
                radius = strokeWidth
            )
        }


    }
}


private fun lerp(a: Float, b: Float, t: Float): Float {
    return a + (b - a) * t
}