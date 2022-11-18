package com.deange.nastychristmas.ui.theme

import androidx.compose.material3.Typography
import com.deange.nastychristmas.ui.compose.appFont

fun AppTypography(): Typography {
  val base = Typography()

  return base.copy(
    displayLarge = base.displayLarge.copy(fontFamily = appFont),
    displayMedium = base.displayMedium.copy(fontFamily = appFont),
    displaySmall = base.displaySmall.copy(fontFamily = appFont),
    headlineLarge = base.headlineLarge.copy(fontFamily = appFont),
    headlineMedium = base.headlineMedium.copy(fontFamily = appFont),
    headlineSmall = base.headlineSmall.copy(fontFamily = appFont),
    titleLarge = base.titleLarge.copy(fontFamily = appFont),
    titleMedium = base.titleMedium.copy(fontFamily = appFont),
    titleSmall = base.titleSmall.copy(fontFamily = appFont),
    bodyLarge = base.bodyLarge.copy(fontFamily = appFont),
    bodyMedium = base.bodyMedium.copy(fontFamily = appFont),
    bodySmall = base.bodySmall.copy(fontFamily = appFont),
    labelLarge = base.labelLarge.copy(fontFamily = appFont),
    labelMedium = base.labelMedium.copy(fontFamily = appFont),
    labelSmall = base.labelSmall.copy(fontFamily = appFont),
  )
}
