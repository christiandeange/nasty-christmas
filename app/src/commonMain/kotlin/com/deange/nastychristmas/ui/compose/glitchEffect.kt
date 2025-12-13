package com.deange.nastychristmas.ui.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.random.nextInt

/**
 * Adapted with love from https://www.sinasamaki.com/glitch-effect/
 */
@Composable
fun Modifier.glitchEffect(
  key: Any? = null,
  glitchColors: List<Color> = listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)),
  random: Random = Random.Default,
): Modifier {
  val graphicsLayer = rememberGraphicsLayer()
  var step by remember { mutableStateOf(0) }

  val slices = remember(key) { random.nextInt(20..40) }
  val fullIntensity = remember(key) { random.nextInt(5, 20) }
  val durationMillis = remember(key) { random.nextInt(200, 1000) }

  LaunchedEffect(key) {
    Animatable(10f)
      .animateTo(
        targetValue = 0f,
        animationSpec = tween(
          durationMillis = durationMillis,
          easing = LinearEasing,
        )
      ) {
        step = this.value.roundToInt()
      }
  }

  return drawWithContent {
    if (step == 0) {
      drawContent()
      return@drawWithContent
    }
    graphicsLayer.record { this@drawWithContent.drawContent() }

    val intensity = step / 10f
    for (i in 0 until slices) {
      translate(
        left = if (random.nextInt(5) < step)
          random.nextInt(-fullIntensity..fullIntensity).toFloat() * intensity
        else
          0f
      ) {
        scale(
          scaleY = 1f,
          scaleX = if (random.nextInt(10) < step)
            1f + (1f * random.nextFloat() * intensity)
          else
            1f
        ) {
          clipRect(
            top = (i / slices.toFloat()) * size.height,
            bottom = (((i + 1) / slices.toFloat()) * size.height) + 1f,
          ) {
            drawLayer(graphicsLayer)
            if (random.nextInt(5, 30) < step) {
              drawRect(
                color = glitchColors.random(),
                blendMode = BlendMode.SrcAtop
              )
            }
          }
        }
      }
    }
  }
}
