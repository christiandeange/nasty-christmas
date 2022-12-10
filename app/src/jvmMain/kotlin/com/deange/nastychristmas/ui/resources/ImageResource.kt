package com.deange.nastychristmas.ui.resources

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

actual class ImageResource actual constructor(
  private val name: String,
) {
  @Composable
  actual fun toPainter(): Painter {
    return painterResource("drawable/$name")
  }
}
