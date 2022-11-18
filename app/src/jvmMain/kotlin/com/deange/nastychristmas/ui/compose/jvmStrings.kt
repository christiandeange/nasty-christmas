package com.deange.nastychristmas.ui.compose

import androidx.compose.runtime.Composable
import dev.icerock.moko.resources.StringResource

@Composable
actual fun StringResource.evaluate(vararg formatArgs: Any): String {
  return localized(args = formatArgs)
}
