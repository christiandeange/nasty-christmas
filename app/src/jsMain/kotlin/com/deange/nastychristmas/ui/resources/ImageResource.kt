package com.deange.nastychristmas.ui.resources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import org.jetbrains.skiko.loadBytesFromPath

actual class ImageResource actual constructor(
  private val name: String,
) {
  @Composable
  actual fun toPainter(): Painter {
    var painter by remember(name) { mutableStateOf<Painter>(BitmapPainter(BlankBitmap)) }

    LaunchedEffect(name) {
      val byteArray = loadBytesFromPath("drawable/$name")
      val image = Image.makeFromEncoded(byteArray)
      painter = BitmapPainter(image.toComposeImageBitmap())
    }

    return painter
  }
}

private val BlankBitmap = ImageBitmap(1, 1)
