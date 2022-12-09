package com.deange.nastychristmas.ui.compose.konfetti.render

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import com.deange.nastychristmas.ui.compose.konfetti.Particle
import com.deange.nastychristmas.ui.compose.konfetti.Party
import com.deange.nastychristmas.ui.compose.konfetti.PartySystem
import com.deange.nastychristmas.util.now
import kotlin.random.Random

@Composable
fun KonfettiView(
  modifier: Modifier = Modifier,
  parties: List<Party>,
  random: Random,
  updateListener: OnParticleSystemUpdateListener? = null,
) {
  /**
   * Particles to draw
   */
  val particles = remember { mutableStateOf(emptyList<Particle>()) }

  /**
   * Latest stored frameTimeMilliseconds
   */
  val frameTime = remember { mutableStateOf(0L) }

  /**
   * Area in which the particles are being drawn
   */
  val drawArea = remember { mutableStateOf(Rect(0f, 0f, 0f, 0f)) }

  val density = LocalDensity.current.density
  LaunchedEffect(Unit) {
    val partySystems = parties.map {
      PartySystem(
        party = it,
        createdAt = now(),
        pixelDensity = density,
        random = random,
      )
    }
    while (true) {
      withFrameMillis { frameMs ->
        // Calculate time between frames, fallback to 0 when previous frame doesn't exist
        val deltaMs = if (frameTime.value > 0) (frameMs - frameTime.value) else 0
        frameTime.value = frameMs

        particles.value = partySystems.map { particleSystem ->

          val totalTimeRunning = getTotalTimeRunning(particleSystem.createdAt)
          // Do not start particleSystem yet if totalTimeRunning is below delay
          if (totalTimeRunning < particleSystem.party.delay) return@map listOf()

          if (particleSystem.isDoneEmitting()) {
            updateListener?.onParticleSystemEnded(
              system = particleSystem,
              activeSystems = partySystems.count { !it.isDoneEmitting() }
            )
          }

          particleSystem.render(deltaMs.div(1000f), drawArea.value)
        }.flatten()
      }
    }
  }

  Canvas(
    modifier = modifier.onGloballyPositioned {
      drawArea.value = Rect(0f, 0f, it.size.width.toFloat(), it.size.height.toFloat())
    },
    onDraw = {
      particles.value.forEach { particle ->
        withTransform({
          rotate(
            degrees = particle.rotation,
            pivot = Offset(
              x = particle.x + (particle.width / 2),
              y = particle.y + (particle.height / 2)
            )
          )
          scale(
            scaleX = particle.scaleX,
            scaleY = 1f,
            pivot = Offset(particle.x + (particle.width / 2), particle.y)
          )
        }) {
          particle.shape.draw(this, particle)
        }
      }
    }
  )
}

fun getTotalTimeRunning(startTime: Long): Long {
  val currentTime = now()
  return (currentTime - startTime)
}
