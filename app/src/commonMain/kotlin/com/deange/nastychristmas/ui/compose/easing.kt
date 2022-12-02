package com.deange.nastychristmas.ui.compose

import androidx.compose.animation.core.CubicBezierEasing

/**
 * Elements exiting a screen use acceleration easing, where they start at rest and
 * end at rest.
 */
val SlowInSlowOutEasing = CubicBezierEasing(0.6f, 0.0f, 0.4f, 1.0f)
