package com.canolabs.rallytransbetxi.ui.miscellaneous

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun Shimmer(content: @Composable (brush: Brush) -> Unit) {
    val transition = rememberInfiniteTransition(label = "")
    val animation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    val colors = listOf(Color.LightGray.copy(alpha = 0.9f), Color.LightGray.copy(alpha = 0.2f), Color.LightGray.copy(alpha = 0.9f))
    val brush = Brush.linearGradient(
        colors = colors,
        start = Offset(10f, 10f),
        end = Offset(animation.value * 2000f, animation.value * 2000f)
    )

    content(brush)
}