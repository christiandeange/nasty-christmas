package com.deange.nastychristmas.ui.compose

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import org.jetbrains.skiko.loadBytesFromPath

actual suspend fun appFont(): FontFamily {
  return FontFamily(
    Font(
      identity = "AvenirNextLTPro-ExtraLight",
      data = loadBytesFromPath("AvenirNextLTPro-ExtraLight.otf"),
      weight = FontWeight.ExtraLight,
      style = FontStyle.Normal,
    ),
    Font(
      identity = "AvenirNextLTPro-ExtraLightItalic",
      data = loadBytesFromPath("AvenirNextLTPro-ExtraLightItalic.otf"),
      weight = FontWeight.ExtraLight,
      style = FontStyle.Italic,
    ),
    Font(
      identity = "AvenirNextLTPro-Regular",
      data = loadBytesFromPath("AvenirNextLTPro-Regular.otf"),
      weight = FontWeight.Normal,
      style = FontStyle.Normal,
    ),
    Font(
      identity = "AvenirNextLTPro-RegularItalic",
      data = loadBytesFromPath("AvenirNextLTPro-RegularItalic.otf"),
      weight = FontWeight.Normal,
      style = FontStyle.Italic,
    ),
    Font(
      identity = "AvenirNextLTPro-Medium",
      data = loadBytesFromPath("AvenirNextLTPro-Medium.otf"),
      weight = FontWeight.Medium,
      style = FontStyle.Normal,
    ),
    Font(
      identity = "AvenirNextLTPro-MediumItalic",
      data = loadBytesFromPath("AvenirNextLTPro-MediumItalic.otf"),
      weight = FontWeight.Medium,
      style = FontStyle.Italic,
    ),
    Font(
      identity = "AvenirNextLTPro-SemiBold",
      data = loadBytesFromPath("AvenirNextLTPro-SemiBold.otf"),
      weight = FontWeight.SemiBold,
      style = FontStyle.Normal,
    ),
    Font(
      identity = "AvenirNextLTPro-SemiBoldItalic",
      data = loadBytesFromPath("AvenirNextLTPro-SemiBoldItalic.otf"),
      weight = FontWeight.SemiBold,
      style = FontStyle.Italic,
    ),
    Font(
      identity = "AvenirNextLTPro-Bold",
      data = loadBytesFromPath("AvenirNextLTPro-Bold.otf"),
      weight = FontWeight.Bold,
      style = FontStyle.Normal,
    ),
    Font(
      identity = "AvenirNextLTPro-BoldItalic",
      data = loadBytesFromPath("AvenirNextLTPro-BoldItalic.otf"),
      weight = FontWeight.Bold,
      style = FontStyle.Italic,
    ),
    Font(
      identity = "AvenirNextLTPro-Heavy",
      data = loadBytesFromPath("AvenirNextLTPro-Heavy.otf"),
      weight = FontWeight.Black,
      style = FontStyle.Normal,
    ),
    Font(
      identity = "AvenirNextLTPro-HeavyItalic",
      data = loadBytesFromPath("AvenirNextLTPro-HeavyItalic.otf"),
      weight = FontWeight.Black,
      style = FontStyle.Italic,
    ),
  )
}

actual suspend fun serifFont(): FontFamily {
  return FontFamily(
    Font(
      identity = "Borgest-Regular",
      data = loadBytesFromPath("Borgest-Regular.ttf"),
      weight = FontWeight.Normal,
      style = FontStyle.Normal,
    ),
    Font(
      identity = "Borgest-RegularItalic",
      data = loadBytesFromPath("Borgest-RegularItalic.ttf"),
      weight = FontWeight.Normal,
      style = FontStyle.Italic,
    ),
  )
}
