package com.jetpack.ratingbar.ratingbar

import androidx.annotation.FloatRange
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun Path.addStar(
    size: Size,
    spikes: Int = 5,
    @FloatRange(from = 0.0, to = 0.5) outerRadiusFraction: Float = 0.5f,
    @FloatRange(from = 0.0, to = 0.5) innerRadiusFraction: Float = 0.2f
): Path {
    val outRadius = size.minDimension * outerRadiusFraction
    val innerRadius = size.minDimension * innerRadiusFraction
    val centerX = size.width / 2
    val centerY = size.height / 2
    var totalAngle = PI / 2
    val degreesPerSection = (2 * PI) / spikes

    moveTo(centerX, 0f)

    var x: Double
    var y: Double

    for (i in 1..spikes) {
        totalAngle += degreesPerSection / 2
        x = centerX + cos(totalAngle) * innerRadius
        y = centerY - sin(totalAngle) * innerRadius
        lineTo(x.toFloat(), y.toFloat())

        totalAngle += degreesPerSection / 2
        x = centerX + cos(totalAngle) * outRadius
        y = centerY - sin(totalAngle) * outRadius
        lineTo(x.toFloat(), y.toFloat())
    }

    close()
    return this
}






















