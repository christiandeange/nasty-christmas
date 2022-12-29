package com.deange.nastychristmas

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.deange.nastychristmas.ui.theme.LocalColorTheme

@Composable
fun StatusBarTheme() {
  val view = LocalView.current
  if (!view.isInEditMode) {
    val colorScheme = MaterialTheme.colorScheme
    val darkTheme = LocalColorTheme.current.isDark()
    SideEffect {
      val window = (view.context as Activity).window
      window.statusBarColor = colorScheme.primary.toArgb()
      WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
    }
  }
}
