package com.deange.nastychristmas.ui.compose

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import com.deange.nastychristmas.core.MR
import org.jetbrains.skiko.loadBytesFromPath

actual suspend fun appFont(): FontFamily {
  return FontFamily(
    Font(
      identity = MR.fonts.Inter.extraLight.fontFamily,
      data = loadBytesFromPath("fonts/Inter-ExtraLight.ttf"),
      weight = FontWeight.ExtraLight,
      style = FontStyle.Normal,
    ),
    Font(
      identity = MR.fonts.Inter.light.fontFamily,
      data = loadBytesFromPath("fonts/Inter-Light.ttf"),
      weight = FontWeight.Light,
      style = FontStyle.Normal,
    ),
    Font(
      identity = MR.fonts.Inter.thin.fontFamily,
      data = loadBytesFromPath("fonts/Inter-Thin.ttf"),
      weight = FontWeight.Thin,
      style = FontStyle.Normal,
    ),
    Font(
      identity = MR.fonts.Inter.regular.fontFamily,
      data = loadBytesFromPath("fonts/Inter-Regular.ttf"),
      weight = FontWeight.Normal,
      style = FontStyle.Normal,
    ),
    Font(
      identity = MR.fonts.Inter.medium.fontFamily,
      data = loadBytesFromPath("fonts/Inter-Medium.ttf"),
      weight = FontWeight.Medium,
      style = FontStyle.Normal,
    ),
    Font(
      identity = MR.fonts.Inter.semiBold.fontFamily,
      data = loadBytesFromPath("fonts/Inter-SemiBold.ttf"),
      weight = FontWeight.SemiBold,
      style = FontStyle.Normal,
    ),
    Font(
      identity = MR.fonts.Inter.bold.fontFamily,
      data = loadBytesFromPath("fonts/Inter-Bold.ttf"),
      weight = FontWeight.Bold,
      style = FontStyle.Normal,
    ),
    Font(
      identity = MR.fonts.Inter.extraBold.fontFamily,
      data = loadBytesFromPath("fonts/Inter-ExtraBold.ttf"),
      weight = FontWeight.ExtraBold,
      style = FontStyle.Normal,
    ),
    Font(
      identity = MR.fonts.Inter.black.fontFamily,
      data = loadBytesFromPath("fonts/Inter-Black.ttf"),
      weight = FontWeight.Black,
      style = FontStyle.Normal,
    ),
  )
}
