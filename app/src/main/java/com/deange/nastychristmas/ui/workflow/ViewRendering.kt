package com.deange.nastychristmas.ui.workflow

import androidx.compose.runtime.Composable

interface ViewRendering {
  @Composable
  fun View(viewEnvironment: ViewEnvironment)
}
