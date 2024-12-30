package com.deange.nastychristmas.settings

import kotlinx.serialization.Serializable

@Serializable
data class GameSettings(
  val gameCode: String,
  val enforceOwnership: Boolean,
) {
  companion object {
    fun default(gameCode: String) = GameSettings(
      gameCode = gameCode,
      enforceOwnership = true,
    )
  }
}
