package com.jetpack.ratingbar.ratingbar

import android.view.MotionEvent
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.*
import com.jetpack.ratingbar.ratingbar.RatingBarUtils.stepSized

sealed class StepSize {
    object ONE : StepSize()
}

sealed class RatingBarStyle {
    object Normal : RatingBarStyle()
    object HighLighted : RatingBarStyle()
}

val StarRatingKey = SemanticsPropertyKey<Float>("StarRating")
var SemanticsPropertyReceiver.starRating by StarRatingKey

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomRatingBar(
    value: Float,
    modifier: Modifier = Modifier,
    config: RatingBarConfig = RatingBarConfig(),
    onValueChange: (Float) -> Unit,
    onRatingChanged: (Float) -> Unit
) {
    var rowSize by remember { mutableStateOf(Size.Zero) }
    var lastDraggedValue by remember { mutableStateOf(0f) }
    val direction = LocalLayoutDirection.current

    Row(modifier = modifier
        .onSizeChanged { rowSize = it.toSize() }
        .pointerInput(
            Unit
        ) {
            detectHorizontalDragGestures(
                onDragEnd = {
                    if (config.isIndicator || config.hideInactiveStars)
                        return@detectHorizontalDragGestures
                    onRatingChanged(lastDraggedValue)
                },
                onDragCancel = {},
                onDragStart = {},
                onHorizontalDrag = { change, _ ->
                    if (config.isIndicator || config.hideInactiveStars)
                        return@detectHorizontalDragGestures
                    change.consumeAllChanges()
                    val x1 = change.position.x.coerceIn(0f, rowSize.width)
                    val calculatedStars =
                        RatingBarUtils.calculateStars(
                            x1,
                            rowSize.width,
                            config.numStars,
                            config.padding.value.toInt()
                        )
                    var newValue =
                        calculatedStars
                            .stepSized(config.stepSize)
                            .coerceIn(0f, config.numStars.toFloat())

                    if (direction == LayoutDirection.Rtl)
                        newValue = config.numStars - newValue
                    onValueChange(newValue)
                    lastDraggedValue = newValue
                }
            )
        }
        .pointerInteropFilter {
            if (config.isIndicator || config.hideInactiveStars)
                return@pointerInteropFilter false
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    //handling when click events
                    val calculatedStars =
                        RatingBarUtils.calculateStars(
                            it.x,
                            rowSize.width,
                            config.numStars,
                            config.padding.value.toInt()
                        )
                    var newValue =
                        calculatedStars
                            .stepSized(config.stepSize)
                            .coerceIn(0f, config.numStars.toFloat())
                    if (direction == LayoutDirection.Rtl)
                        newValue = config.numStars - newValue
                    onValueChange(newValue)
                    onRatingChanged(newValue)
                }
            }
            true
        }) {
        ComposeStars(value, config)
    }
}

@Composable
fun ComposeStars(
    value: Float,
    config: RatingBarConfig
) {

    val ratingPerStar = 1f
    var remainingRating = value

    Row(modifier = Modifier
        .semantics { starRating = value }) {
        for (i in 1..config.numStars) {
            val starRating = when {
                remainingRating == 0f -> {
                    0f
                }
                remainingRating >= ratingPerStar -> {
                    remainingRating -= ratingPerStar
                    1f
                }
                else -> {
                    val fraction = remainingRating / ratingPerStar
                    remainingRating = 0f
                    fraction
                }
            }
            if (config.hideInactiveStars && starRating == 0.0f)
                break
            RatingStar(
                fraction = starRating,
                config = config,
                modifier = Modifier
                    .padding(
                        start = if (i > 1) config.padding else 0.dp,
                        end = if (i < config.numStars) config.padding else 0.dp
                    )
                    .size(size = config.size)
                    .testTag("RatingStar")
            )
        }
    }
}