package com.deange.nastychristmas.ui.compose

import androidx.compose.ui.text.font.FontFamily

lateinit var appFont: FontFamily
  private set

expect suspend fun appFont(): FontFamily

suspend fun initTypography() {
  appFont = appFont()
}
