package com.deange.nastychristmas.ui.compose

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun Modifier.christmasBackground(): Modifier = background(rememberLoginGradientBrush())
