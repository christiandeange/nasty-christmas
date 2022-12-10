package com.deange.nastychristmas.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import com.deange.nastychristmas.ui.resources.EN
import com.deange.nastychristmas.ui.resources.StringResources

@Composable
fun Language(
  strings: StringResources,
  content: @Composable () -> Unit,
) {
  CompositionLocalProvider(LocalStrings provides strings) {
    content()
  }
}

val Strings: StringResources
  @Composable get() = LocalStrings.current

private val LocalStrings = compositionLocalOf<StringResources> {
  StringResources.EN
}
