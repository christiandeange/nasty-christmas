package com.deange.nastychristmas.ui.resources

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

expect class ImageResource(name: String) {
  @Composable
  fun toPainter(): Painter
}
