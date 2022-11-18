package com.deange.nastychristmas.ui.compose

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import com.deange.nastychristmas.core.MR

actual suspend fun appFont(): FontFamily {
  return FontFamily(
    Font(
      file = MR.fonts.Inter.extraLight.file,
      weight = FontWeight.ExtraLight,
      style = FontStyle.Normal,
    ),
    Font(
      file = MR.fonts.Inter.light.file,
      weight = FontWeight.Light,
      style = FontStyle.Normal,
    ),
    Font(
      file = MR.fonts.Inter.thin.file,
      weight = FontWeight.Thin,
      style = FontStyle.Normal,
    ),
    Font(
      file = MR.fonts.Inter.regular.file,
      weight = FontWeight.Normal,
      style = FontStyle.Normal,
    ),
    Font(
      file = MR.fonts.Inter.medium.file,
      weight = FontWeight.Medium,
      style = FontStyle.Normal,
    ),
    Font(
      file = MR.fonts.Inter.semiBold.file,
      weight = FontWeight.SemiBold,
      style = FontStyle.Normal,
    ),
    Font(
      file = MR.fonts.Inter.bold.file,
      weight = FontWeight.Bold,
      style = FontStyle.Normal,
    ),
    Font(
      file = MR.fonts.Inter.extraBold.file,
      weight = FontWeight.ExtraBold,
      style = FontStyle.Normal,
    ),
    Font(
      file = MR.fonts.Inter.black.file,
      weight = FontWeight.Black,
      style = FontStyle.Normal,
    ),
  )
}
