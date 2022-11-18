package com.deange.nastychristmas.ui.compose

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.deange.nastychristmas.core.MR

actual suspend fun appFont(): FontFamily {
  return FontFamily(
    Font(
      resId = MR.fonts.Inter.extraLight.fontResourceId,
      weight = FontWeight.ExtraLight,
      style = FontStyle.Normal,
    ),
    Font(
      resId = MR.fonts.Inter.light.fontResourceId,
      weight = FontWeight.Light,
      style = FontStyle.Normal,
    ),
    Font(
      resId = MR.fonts.Inter.thin.fontResourceId,
      weight = FontWeight.Thin,
      style = FontStyle.Normal,
    ),
    Font(
      resId = MR.fonts.Inter.regular.fontResourceId,
      weight = FontWeight.Normal,
      style = FontStyle.Normal,
    ),
    Font(
      resId = MR.fonts.Inter.medium.fontResourceId,
      weight = FontWeight.Medium,
      style = FontStyle.Normal,
    ),
    Font(
      resId = MR.fonts.Inter.semiBold.fontResourceId,
      weight = FontWeight.SemiBold,
      style = FontStyle.Normal,
    ),
    Font(
      resId = MR.fonts.Inter.bold.fontResourceId,
      weight = FontWeight.Bold,
      style = FontStyle.Normal,
    ),
    Font(
      resId = MR.fonts.Inter.extraBold.fontResourceId,
      weight = FontWeight.ExtraBold,
      style = FontStyle.Normal,
    ),
    Font(
      resId = MR.fonts.Inter.black.fontResourceId,
      weight = FontWeight.Black,
      style = FontStyle.Normal,
    ),
  )
}
