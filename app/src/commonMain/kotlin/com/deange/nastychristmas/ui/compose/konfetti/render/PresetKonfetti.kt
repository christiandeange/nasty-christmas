package com.deange.nastychristmas.ui.compose.konfetti.render

import com.deange.nastychristmas.ui.compose.konfetti.Angle
import com.deange.nastychristmas.ui.compose.konfetti.Party
import com.deange.nastychristmas.ui.compose.konfetti.Position.Relative
import com.deange.nastychristmas.ui.compose.konfetti.Rotation
import com.deange.nastychristmas.ui.compose.konfetti.Spread
import com.deange.nastychristmas.ui.compose.konfetti.emitter.Emitter
import com.deange.nastychristmas.ui.compose.konfetti.models.Size
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

object PresetKonfetti {
  private val DefaultColors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def)

  fun festive(): List<Party> {
    val party = Party(
      speed = 30f,
      maxSpeed = 50f,
      damping = 0.9f,
      angle = Angle.TOP,
      spread = 90,
      size = listOf(Size.SMALL, Size.LARGE),
      timeToLive = 3000L,
      rotation = Rotation(),
      colors = DefaultColors,
      emitter = Emitter(duration = 100.milliseconds).max(30),
      position = Relative(0.5, 1.0)
    )

    return listOf(
      party,
      party.copy(
        speed = 55f,
        maxSpeed = 65f,
        spread = 10,
        emitter = Emitter(duration = 100.milliseconds).max(10),
      ),
      party.copy(
        speed = 50f,
        maxSpeed = 60f,
        spread = 120,
        emitter = Emitter(duration = 100.milliseconds).max(40),
      ),
      party.copy(
        speed = 65f,
        maxSpeed = 80f,
        spread = 10,
        emitter = Emitter(duration = 100.milliseconds).max(10),
      )
    )
  }

  fun explode(): List<Party> {
    return listOf(
      Party(
        speed = 0f,
        maxSpeed = 30f,
        damping = 0.9f,
        spread = 360,
        colors = DefaultColors,
        emitter = Emitter(duration = 100.milliseconds).max(100),
        position = Relative(0.5, 0.4),
      )
    )
  }

  fun parade(): List<Party> {
    val party = Party(
      speed = 10f,
      maxSpeed = 30f,
      damping = 0.9f,
      angle = Angle.RIGHT - 45,
      spread = Spread.SMALL,
      colors = DefaultColors,
      emitter = Emitter(duration = 5.seconds).perSecond(30),
      position = Relative(0.0, 0.5),
    )

    return listOf(
      party,
      party.copy(
        angle = party.angle - 90, // flip angle from right to left
        position = Relative(1.0, 0.5),
      ),
    )
  }

  fun rain(): List<Party> {
    return listOf(
      Party(
        speed = 0f,
        maxSpeed = 15f,
        damping = 0.9f,
        angle = Angle.BOTTOM,
        spread = Spread.ROUND,
        colors = DefaultColors,
        emitter = Emitter(duration = 5.seconds).perSecond(100),
        position = Relative(0.0, 0.0).between(Relative(1.0, 0.0)),
      )
    )
  }
}
