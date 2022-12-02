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
      identity = MR.fonts.AvenirNextLTPro.extraLight.fontFamily,
      data = loadBytesFromPath("fonts/AvenirNextLTPro-ExtraLight.ttf"),
      weight = FontWeight.ExtraLight,
      style = FontStyle.Normal,
    ),
    Font(
      identity = MR.fonts.AvenirNextLTPro.extraLight.fontFamily,
      data = loadBytesFromPath("fonts/AvenirNextLTPro-ExtraLightItalic.ttf"),
      weight = FontWeight.ExtraLight,
      style = FontStyle.Italic,
    ),
    Font(
      identity = MR.fonts.AvenirNextLTPro.regular.fontFamily,
      data = loadBytesFromPath("fonts/AvenirNextLTPro-Regular.ttf"),
      weight = FontWeight.Normal,
      style = FontStyle.Normal,
    ),
    Font(
      identity = MR.fonts.AvenirNextLTPro.regular.fontFamily,
      data = loadBytesFromPath("fonts/AvenirNextLTPro-RegularItalic.ttf"),
      weight = FontWeight.Normal,
      style = FontStyle.Italic,
    ),
    Font(
      identity = MR.fonts.AvenirNextLTPro.medium.fontFamily,
      data = loadBytesFromPath("fonts/AvenirNextLTPro-Medium.ttf"),
      weight = FontWeight.Medium,
      style = FontStyle.Normal,
    ),
    Font(
      identity = MR.fonts.AvenirNextLTPro.medium.fontFamily,
      data = loadBytesFromPath("fonts/AvenirNextLTPro-MediumItalic.ttf"),
      weight = FontWeight.Medium,
      style = FontStyle.Italic,
    ),
    Font(
      identity = MR.fonts.AvenirNextLTPro.semiBold.fontFamily,
      data = loadBytesFromPath("fonts/AvenirNextLTPro-SemiBold.ttf"),
      weight = FontWeight.SemiBold,
      style = FontStyle.Normal,
    ),
    Font(
      identity = MR.fonts.AvenirNextLTPro.semiBold.fontFamily,
      data = loadBytesFromPath("fonts/AvenirNextLTPro-SemiBoldItalic.ttf"),
      weight = FontWeight.SemiBold,
      style = FontStyle.Italic,
    ),
    Font(
      identity = MR.fonts.AvenirNextLTPro.bold.fontFamily,
      data = loadBytesFromPath("fonts/AvenirNextLTPro-Bold.ttf"),
      weight = FontWeight.Bold,
      style = FontStyle.Normal,
    ),
    Font(
      identity = MR.fonts.AvenirNextLTPro.bold.fontFamily,
      data = loadBytesFromPath("fonts/AvenirNextLTPro-BoldItalic.ttf"),
      weight = FontWeight.Bold,
      style = FontStyle.Italic,
    ),
    Font(
      identity = MR.fonts.AvenirNextLTPro.heavy.fontFamily,
      data = loadBytesFromPath("fonts/AvenirNextLTPro-Heavy.ttf"),
      weight = FontWeight.Black,
      style = FontStyle.Normal,
    ),
    Font(
      identity = MR.fonts.AvenirNextLTPro.heavy.fontFamily,
      data = loadBytesFromPath("fonts/AvenirNextLTPro-HeavyItalic.ttf"),
      weight = FontWeight.Black,
      style = FontStyle.Italic,
    ),
  )
}

actual suspend fun serifFont(): FontFamily {
  return FontFamily(
    Font(
      identity = MR.fonts.Borgest.regular.fontFamily,
      data = loadBytesFromPath("fonts/Borgest-Regular.ttf"),
      weight = FontWeight.Normal,
      style = FontStyle.Normal,
    ),
    Font(
      identity = MR.fonts.Borgest.regularItalic.fontFamily,
      data = loadBytesFromPath("fonts/Borgest-RegularItalic.ttf"),
      weight = FontWeight.Normal,
      style = FontStyle.Italic,
    ),
  )
}
