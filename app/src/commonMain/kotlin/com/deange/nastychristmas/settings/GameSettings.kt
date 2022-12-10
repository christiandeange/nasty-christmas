package com.deange.nastychristmas.settings

import kotlinx.serialization.Serializable

@Serializable
data class GameSettings(
  val enforceOwnership: Boolean,
) {
  companion object {
    val Default = GameSettings(enforceOwnership = true)
  }
}
