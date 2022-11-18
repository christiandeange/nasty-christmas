package com.deange.nastychristmas.ui.compose

import androidx.compose.runtime.Composable
import androidx.activity.compose.BackHandler as AndroidBackHandler

@Composable
actual fun BackHandler(onBack: () -> Unit) {
  AndroidBackHandler { onBack() }
}
