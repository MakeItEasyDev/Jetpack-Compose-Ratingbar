package com.jetpack.ratingbar.ratingbar

import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

private const val strokeWidth = 1f

@Composable
fun RatingStar(
    @FloatRange(from = 0.0, to = 1.0) fraction: Float,
    config: RatingBarConfig,
    modifier: Modifier = Modifier,
) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    Box(modifier = modifier) {
        FilledStar(fraction, config.activeColor, isRtl)
        EmptyStar(fraction, config, isRtl)
    }
}

@Composable
private fun FilledStar(fraction: Float, activeColor: Color, isRtl: Boolean) = Canvas(
    modifier = Modifier
        .fillMaxSize()
        .clip(
            if (isRtl)
                rtlFilledStarFractionalShape(fraction = fraction)
            else
                FractionalRectangleShape(0f, fraction)
        )
) {
    val path = Path().addStar(size)

    drawPath(path, color = activeColor, style = Fill) // Filled Star
    drawPath(path, color = activeColor, style = Stroke(width = strokeWidth)) // Border
}

@Composable
private fun EmptyStar(
    fraction: Float,
    config: RatingBarConfig,
    isRtl: Boolean
) = Canvas(
        modifier = Modifier
            .fillMaxSize()
            .clip(
                if (isRtl)
                    rtlEmptyStarFractionalShape(fraction = fraction)
                else
                    FractionalRectangleShape(fraction, 1f)
            )
    ) {
        val path = Path().addStar(size)
        if (config.style is RatingBarStyle.Normal)
            drawPath(path, color = config.inactiveColor, style = Fill) 
        else
            drawPath(path, color = Color.Gray, style = Stroke(width = strokeWidth))
    }

fun rtlEmptyStarFractionalShape(fraction: Float): FractionalRectangleShape {
    return if (fraction == 1f || fraction == 0f)
        FractionalRectangleShape(fraction, 1f)
    else FractionalRectangleShape(0f, 1f - fraction)
}

fun rtlFilledStarFractionalShape(fraction: Float): FractionalRectangleShape {
    return if (fraction == 0f || fraction == 1f)
        FractionalRectangleShape(0f, fraction)
    else FractionalRectangleShape(1f - fraction, 1f)
}