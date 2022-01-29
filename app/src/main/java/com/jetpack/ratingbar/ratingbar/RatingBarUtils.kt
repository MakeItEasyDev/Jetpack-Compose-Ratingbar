package com.jetpack.ratingbar.ratingbar

import kotlin.math.roundToInt

object RatingBarUtils {
    fun calculateStars(
        draggedWidth: Float,
        width: Float,
        numStars: Int,
        padding: Int
    ): Float {
        var overAllComposeWidth = width
        val spacerWidth = numStars * (2 * padding)
        overAllComposeWidth -= spacerWidth
        return if (draggedWidth != 0f) ((draggedWidth / overAllComposeWidth) * numStars) else 0f
    }

    fun Float.stepSized(stepSize: StepSize): Float {
        return if (stepSize is StepSize.ONE)
            this.roundToInt().toFloat()
        else {
            var value = this.toInt().toFloat()
            if (this < value.plus(0.5)) {
                if (this == 0f)
                    return 0f
                value = value.plus(0.5).toFloat()
                value
            } else {
                this.roundToInt().toFloat()
            }
        }
    }
}



















