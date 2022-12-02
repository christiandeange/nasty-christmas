package com.deange.nastychristmas.ui.compose

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.deange.nastychristmas.core.MR

actual suspend fun appFont(): FontFamily {
  return FontFamily(
    Font(
      resId = MR.fonts.AvenirNextLTPro.extraLight.fontResourceId,
      weight = FontWeight.ExtraLight,
      style = FontStyle.Normal,
    ),
    Font(
      resId = MR.fonts.AvenirNextLTPro.extraLight.fontResourceId,
      weight = FontWeight.ExtraLight,
      style = FontStyle.Italic,
    ),
    Font(
      resId = MR.fonts.AvenirNextLTPro.regular.fontResourceId,
      weight = FontWeight.Normal,
      style = FontStyle.Normal,
    ),
    Font(
      resId = MR.fonts.AvenirNextLTPro.regular.fontResourceId,
      weight = FontWeight.Normal,
      style = FontStyle.Italic,
    ),
    Font(
      resId = MR.fonts.AvenirNextLTPro.medium.fontResourceId,
      weight = FontWeight.Medium,
      style = FontStyle.Normal,
    ),
    Font(
      resId = MR.fonts.AvenirNextLTPro.medium.fontResourceId,
      weight = FontWeight.Medium,
      style = FontStyle.Italic,
    ),
    Font(
      resId = MR.fonts.AvenirNextLTPro.semiBold.fontResourceId,
      weight = FontWeight.SemiBold,
      style = FontStyle.Normal,
    ),
    Font(
      resId = MR.fonts.AvenirNextLTPro.semiBold.fontResourceId,
      weight = FontWeight.SemiBold,
      style = FontStyle.Italic,
    ),
    Font(
      resId = MR.fonts.AvenirNextLTPro.bold.fontResourceId,
      weight = FontWeight.Bold,
      style = FontStyle.Normal,
    ),
    Font(
      resId = MR.fonts.AvenirNextLTPro.bold.fontResourceId,
      weight = FontWeight.Bold,
      style = FontStyle.Italic,
    ),
    Font(
      resId = MR.fonts.AvenirNextLTPro.heavy.fontResourceId,
      weight = FontWeight.Black,
      style = FontStyle.Normal,
    ),
    Font(
      resId = MR.fonts.AvenirNextLTPro.heavy.fontResourceId,
      weight = FontWeight.Black,
      style = FontStyle.Italic,
    ),
  )
}

actual suspend fun serifFont(): FontFamily {
  return FontFamily(
    Font(
      resId = MR.fonts.Borgest.regular.fontResourceId,
      weight = FontWeight.Normal,
      style = FontStyle.Normal,
    ),
    Font(
      resId = MR.fonts.Borgest.regularItalic.fontResourceId,
      weight = FontWeight.Normal,
      style = FontStyle.Italic,
    ),
  )
}
