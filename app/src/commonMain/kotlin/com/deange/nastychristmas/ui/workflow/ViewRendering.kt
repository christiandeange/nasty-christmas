package com.deange.nastychristmas.ui.workflow

import androidx.compose.runtime.Composable

interface ViewRendering {
  @Composable
  fun Content()

  companion object {
    val Empty = object : ViewRendering {
      @Composable
      override fun Content() = Unit
    }
  }
}
