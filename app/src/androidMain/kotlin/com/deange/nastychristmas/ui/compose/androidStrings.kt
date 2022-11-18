package com.deange.nastychristmas.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.icerock.moko.resources.StringResource

@Composable
actual fun StringResource.evaluate(vararg formatArgs: Any): String {
  return stringResource(resourceId, *formatArgs)
}
