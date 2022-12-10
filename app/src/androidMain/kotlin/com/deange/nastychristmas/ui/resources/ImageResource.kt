package com.deange.nastychristmas.ui.resources

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

actual class ImageResource actual constructor(
  private val name: String,
) {
  @SuppressLint("DiscouragedApi")
  @Composable
  actual fun toPainter(): Painter {
    val resourceName = name.substringBeforeLast(".")
    val context = LocalContext.current
    val packageName = context.packageName

    val resourceId = context.resources.getIdentifier(resourceName, "drawable", packageName)
    return painterResource(resourceId)
  }
}
