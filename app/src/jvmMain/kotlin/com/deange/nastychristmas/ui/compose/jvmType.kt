package com.deange.nastychristmas.ui.compose

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream

actual suspend fun appFont(): FontFamily {
  return FontFamily(
    Font(
      file = file("AvenirNextLTPro-ExtraLight.otf"),
      weight = FontWeight.ExtraLight,
      style = FontStyle.Normal,
    ),
    Font(
      file = file("AvenirNextLTPro-ExtraLightItalic.otf"),
      weight = FontWeight.ExtraLight,
      style = FontStyle.Italic,
    ),
    Font(
      file = file("AvenirNextLTPro-Regular.otf"),
      weight = FontWeight.Normal,
      style = FontStyle.Normal,
    ),
    Font(
      file = file("AvenirNextLTPro-RegularItalic.otf"),
      weight = FontWeight.Normal,
      style = FontStyle.Italic,
    ),
    Font(
      file = file("AvenirNextLTPro-Medium.otf"),
      weight = FontWeight.Medium,
      style = FontStyle.Normal,
    ),
    Font(
      file = file("AvenirNextLTPro-MediumItalic.otf"),
      weight = FontWeight.Medium,
      style = FontStyle.Italic,
    ),
    Font(
      file = file("AvenirNextLTPro-SemiBold.otf"),
      weight = FontWeight.SemiBold,
      style = FontStyle.Normal,
    ),
    Font(
      file = file("AvenirNextLTPro-SemiBoldItalic.otf"),
      weight = FontWeight.SemiBold,
      style = FontStyle.Italic,
    ),
    Font(
      file = file("AvenirNextLTPro-Bold.otf"),
      weight = FontWeight.Bold,
      style = FontStyle.Normal,
    ),
    Font(
      file = file("AvenirNextLTPro-BoldItalic.otf"),
      weight = FontWeight.Bold,
      style = FontStyle.Italic,
    ),
    Font(
      file = file("AvenirNextLTPro-Heavy.otf"),
      weight = FontWeight.Black,
      style = FontStyle.Normal,
    ),
    Font(
      file = file("AvenirNextLTPro-HeavyItalic.otf"),
      weight = FontWeight.Black,
      style = FontStyle.Italic,
    ),
  )
}

actual suspend fun serifFont(): FontFamily {
  return FontFamily(
    Font(
      file = file("Borgest-Regular.ttf"),
      weight = FontWeight.Normal,
      style = FontStyle.Normal,
    ),
    Font(
      file = file("Borgest-RegularItalic.ttf"),
      weight = FontWeight.Normal,
      style = FontStyle.Italic,
    ),
  )
}

private fun file(fileName: String): File {
  val resourceStream: InputStream =
    Thread.currentThread().contextClassLoader.getResourceAsStream(fileName)
      ?: throw FileNotFoundException("Couldn't find font resource at: $fileName")

  return resourceStream.use { inputStream ->
    val file = File.createTempFile("font-cache", null)
    file.deleteOnExit()

    file.outputStream().use { outputStream ->
      inputStream.copyTo(outputStream)
    }

    file
  }
}
