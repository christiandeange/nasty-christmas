package com.deange.nastychristmas.ui.compose

import androidx.compose.runtime.Composable
import com.deange.nastychristmas.core.MR
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.provider.JsStringProvider

private lateinit var strings: JsStringProvider

suspend fun initResources() {
  initTypography()
  strings = MR.stringsLoader.getOrLoad()
}

@Composable
actual fun StringResource.evaluate(vararg formatArgs: Any): String {
  return localized(strings, null, *formatArgs)
}
