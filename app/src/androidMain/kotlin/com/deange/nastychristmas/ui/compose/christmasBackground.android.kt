package com.deange.nastychristmas.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mikepenz.hypnoticcanvas.shaderBackground
import com.mikepenz.hypnoticcanvas.shaders.Shader

@Composable
actual fun Modifier.christmasBackground(): Modifier = shaderBackground(ChristmasGradient)

object ChristmasGradient : Shader {
  override val name: String = "ChristmasGradient"
  override val authorName: String = ""
  override val authorUrl: String = ""
  override val credit: String = ""
  override val license: String = ""
  override val licenseUrl: String = ""

  override val sksl = christmasSksl
}
