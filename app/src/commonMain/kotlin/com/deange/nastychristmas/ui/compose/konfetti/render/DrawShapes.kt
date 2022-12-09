package com.deange.nastychristmas.ui.compose.konfetti.render

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.deange.nastychristmas.ui.compose.konfetti.Particle
import com.deange.nastychristmas.ui.compose.konfetti.models.Shape

/**
 * Draw a shape to `compose canvas`. Implementations are expected to draw within a square of size
 * `size` and must vertically/horizontally center their asset if it does not have an equal width
 * and height.
 */
fun Shape.draw(drawScope: DrawScope, particle: Particle) {
  when (this) {
    is Shape.Circle -> {
      val offsetMiddle = particle.width / 2
      drawScope.drawCircle(
        color = Color(particle.color),
        center = Offset(particle.x + offsetMiddle, particle.y + offsetMiddle),
        radius = particle.width / 2
      )
    }
    is Shape.Square -> {
      drawScope.drawRect(
        color = Color(particle.color),
        topLeft = Offset(particle.x, particle.y),
        size = Size(particle.width, particle.height),
      )
    }
    is Shape.Rectangle -> {
      val size = particle.width
      val height = size * heightRatio
      drawScope.drawRect(
        color = Color(particle.color),
        topLeft = Offset(particle.x, particle.y),
        size = Size(size, height)
      )
    }
  }
}
