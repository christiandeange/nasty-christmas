package com.deange.nastychristmas.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import dev.icerock.moko.resources.ImageResource

@Composable
actual fun ImageResource.toPainter(): Painter? {
  return null
}
