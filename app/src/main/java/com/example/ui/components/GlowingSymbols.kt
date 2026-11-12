package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonCyanGlow
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPinkGlow

@Composable
fun GlowingX(
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    color: Color = NeonCyan,
    glowColor: Color = NeonCyanGlow,
    animate: Boolean = true
) {
    val scale = remember { Animatable(if (animate) 0.2f else 1f) }
    val alpha = remember { Animatable(if (animate) 0f else 1f) }

    if (animate) {
        LaunchedEffect(Unit) {
            scale.animateTo(1f, animationSpec = tween(280, easing = FastOutSlowInEasing))
        }
        LaunchedEffect(Unit) {
            alpha.animateTo(1f, animationSpec = tween(220))
        }
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = this.size.width * scale.value
            val canvasHeight = this.size.height * scale.value
            val currentAlpha = alpha.value

            val padX = (this.size.width - canvasWidth) / 2f + canvasWidth * 0.20f
            val padY = (this.size.height - canvasHeight) / 2f + canvasHeight * 0.20f
            val endX = (this.size.width + canvasWidth) / 2f - canvasWidth * 0.20f
            val endY = (this.size.height + canvasHeight) / 2f - canvasHeight * 0.20f

            val strokeWidth = canvasWidth * 0.12f

            // Layer 1: Outer soft glow
            val outerGlowAlpha = (0.35f * currentAlpha).coerceIn(0f, 1f)
            drawLine(
                color = glowColor.copy(alpha = outerGlowAlpha),
                start = Offset(padX, padY),
                end = Offset(endX, endY),
                strokeWidth = strokeWidth * 2.4f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = glowColor.copy(alpha = outerGlowAlpha),
                start = Offset(endX, padY),
                end = Offset(padX, endY),
                strokeWidth = strokeWidth * 2.4f,
                cap = StrokeCap.Round
            )

            // Layer 2: Mid glow
            val midGlowAlpha = (0.65f * currentAlpha).coerceIn(0f, 1f)
            drawLine(
                color = glowColor.copy(alpha = midGlowAlpha),
                start = Offset(padX, padY),
                end = Offset(endX, endY),
                strokeWidth = strokeWidth * 1.5f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = glowColor.copy(alpha = midGlowAlpha),
                start = Offset(endX, padY),
                end = Offset(padX, endY),
                strokeWidth = strokeWidth * 1.5f,
                cap = StrokeCap.Round
            )

            // Layer 3: Sharp luminous core
            val coreAlpha = (1f * currentAlpha).coerceIn(0f, 1f)
            drawLine(
                color = color.copy(alpha = coreAlpha),
                start = Offset(padX, padY),
                end = Offset(endX, endY),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
            drawLine(
                color = color.copy(alpha = coreAlpha),
                start = Offset(endX, padY),
                end = Offset(padX, endY),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun GlowingO(
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    color: Color = NeonPink,
    glowColor: Color = NeonPinkGlow,
    animate: Boolean = true
) {
    val scale = remember { Animatable(if (animate) 0.2f else 1f) }
    val alpha = remember { Animatable(if (animate) 0f else 1f) }

    if (animate) {
        LaunchedEffect(Unit) {
            scale.animateTo(1f, animationSpec = tween(280, easing = FastOutSlowInEasing))
        }
        LaunchedEffect(Unit) {
            alpha.animateTo(1f, animationSpec = tween(220))
        }
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = this.size.width * scale.value
            val currentAlpha = alpha.value
            val radius = (canvasWidth * 0.32f).coerceAtLeast(1f)
            val strokeWidth = canvasWidth * 0.12f
            val centerOffset = Offset(this.size.width / 2f, this.size.height / 2f)

            // Layer 1: Outer soft glow
            val outerGlowAlpha = (0.35f * currentAlpha).coerceIn(0f, 1f)
            drawCircle(
                color = glowColor.copy(alpha = outerGlowAlpha),
                radius = radius,
                center = centerOffset,
                style = Stroke(width = strokeWidth * 2.4f, cap = StrokeCap.Round)
            )

            // Layer 2: Mid glow
            val midGlowAlpha = (0.65f * currentAlpha).coerceIn(0f, 1f)
            drawCircle(
                color = glowColor.copy(alpha = midGlowAlpha),
                radius = radius,
                center = centerOffset,
                style = Stroke(width = strokeWidth * 1.5f, cap = StrokeCap.Round)
            )

            // Layer 3: Sharp luminous core
            val coreAlpha = (1f * currentAlpha).coerceIn(0f, 1f)
            drawCircle(
                color = color.copy(alpha = coreAlpha),
                radius = radius,
                center = centerOffset,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
    }
}
