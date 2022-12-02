package com.deange.nastychristmas.ui.compose

import androidx.compose.ui.text.font.FontFamily

lateinit var appFont: FontFamily
  private set
lateinit var serifFont: FontFamily
  private set

expect suspend fun appFont(): FontFamily
expect suspend fun serifFont(): FontFamily

suspend fun initTypography() {
  appFont = appFont()
  serifFont = serifFont()
}
