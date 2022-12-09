package com.deange.nastychristmas.ui.compose

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import com.deange.nastychristmas.core.MR

actual suspend fun appFont(): FontFamily {
  return FontFamily(
    Font(
      file = MR.fonts.AvenirNextLTPro.extraLight.file,
      weight = FontWeight.ExtraLight,
      style = FontStyle.Normal,
    ),
    Font(
      file = MR.fonts.AvenirNextLTPro.extraLight.file,
      weight = FontWeight.ExtraLight,
      style = FontStyle.Italic,
    ),
    Font(
      file = MR.fonts.AvenirNextLTPro.regular.file,
      weight = FontWeight.Normal,
      style = FontStyle.Normal,
    ),
    Font(
      file = MR.fonts.AvenirNextLTPro.regular.file,
      weight = FontWeight.Normal,
      style = FontStyle.Italic,
    ),
    Font(
      file = MR.fonts.AvenirNextLTPro.medium.file,
      weight = FontWeight.Medium,
      style = FontStyle.Normal,
    ),
    Font(
      file = MR.fonts.AvenirNextLTPro.medium.file,
      weight = FontWeight.Medium,
      style = FontStyle.Italic,
    ),
    Font(
      file = MR.fonts.AvenirNextLTPro.semiBold.file,
      weight = FontWeight.SemiBold,
      style = FontStyle.Normal,
    ),
    Font(
      file = MR.fonts.AvenirNextLTPro.semiBold.file,
      weight = FontWeight.SemiBold,
      style = FontStyle.Italic,
    ),
    Font(
      file = MR.fonts.AvenirNextLTPro.bold.file,
      weight = FontWeight.Bold,
      style = FontStyle.Normal,
    ),
    Font(
      file = MR.fonts.AvenirNextLTPro.bold.file,
      weight = FontWeight.Bold,
      style = FontStyle.Italic,
    ),
    Font(
      file = MR.fonts.AvenirNextLTPro.heavy.file,
      weight = FontWeight.Black,
      style = FontStyle.Normal,
    ),
    Font(
      file = MR.fonts.AvenirNextLTPro.heavy.file,
      weight = FontWeight.Black,
      style = FontStyle.Italic,
    ),
  )
}

actual suspend fun serifFont(): FontFamily {
  return FontFamily(
    Font(
      file = MR.fonts.Borgest.regular.file,
      weight = FontWeight.Normal,
      style = FontStyle.Normal,
    ),
    Font(
      file = MR.fonts.Borgest.regularItalic.file,
      weight = FontWeight.Normal,
      style = FontStyle.Italic,
    ),
  )
}
