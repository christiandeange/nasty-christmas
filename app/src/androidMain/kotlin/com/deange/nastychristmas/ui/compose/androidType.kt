@file:OptIn(ExperimentalTextApi::class)

package com.deange.nastychristmas.ui.compose

import android.annotation.SuppressLint
import android.content.res.AssetManager
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

@SuppressLint("StaticFieldLeak")
lateinit var fontsAssetManager: AssetManager

actual suspend fun appFont(): FontFamily {
  return FontFamily(
    Font(
      "AvenirNextLTPro-ExtraLight.otf",
      fontsAssetManager,
      weight = FontWeight.ExtraLight,
      style = FontStyle.Normal,
    ),
    Font(
      "AvenirNextLTPro-ExtraLightItalic.otf",
      fontsAssetManager,
      weight = FontWeight.ExtraLight,
      style = FontStyle.Italic,
    ),
    Font(
      "AvenirNextLTPro-Regular.otf",
      fontsAssetManager,
      weight = FontWeight.Normal,
      style = FontStyle.Normal,
    ),
    Font(
      "AvenirNextLTPro-RegularItalic.otf",
      fontsAssetManager,
      weight = FontWeight.Normal,
      style = FontStyle.Italic,
    ),
    Font(
      "AvenirNextLTPro-Medium.otf",
      fontsAssetManager,
      weight = FontWeight.Medium,
      style = FontStyle.Normal,
    ),
    Font(
      "AvenirNextLTPro-MediumItalic.otf",
      fontsAssetManager,
      weight = FontWeight.Medium,
      style = FontStyle.Italic,
    ),
    Font(
      "AvenirNextLTPro-SemiBold.otf",
      fontsAssetManager,
      weight = FontWeight.SemiBold,
      style = FontStyle.Normal,
    ),
    Font(
      "AvenirNextLTPro-SemiBoldItalic.otf",
      fontsAssetManager,
      weight = FontWeight.SemiBold,
      style = FontStyle.Italic,
    ),
    Font(
      "AvenirNextLTPro-Bold.otf",
      fontsAssetManager,
      weight = FontWeight.Bold,
      style = FontStyle.Normal,
    ),
    Font(
      "AvenirNextLTPro-BoldItalic.otf",
      fontsAssetManager,
      weight = FontWeight.Bold,
      style = FontStyle.Italic,
    ),
    Font(
      "AvenirNextLTPro-Heavy.otf",
      fontsAssetManager,
      weight = FontWeight.Black,
      style = FontStyle.Normal,
    ),
    Font(
      "AvenirNextLTPro-HeavyItalic.otf",
      fontsAssetManager,
      weight = FontWeight.Black,
      style = FontStyle.Italic,
    ),
  )
}

actual suspend fun serifFont(): FontFamily {
  return FontFamily(
    Font(
      "Borgest-Regular.ttf",
      fontsAssetManager,
      weight = FontWeight.Normal,
      style = FontStyle.Normal,
    ),
    Font(
      "Borgest-RegularItalic.ttf",
      fontsAssetManager,
      weight = FontWeight.Normal,
      style = FontStyle.Italic,
    ),
  )
}
