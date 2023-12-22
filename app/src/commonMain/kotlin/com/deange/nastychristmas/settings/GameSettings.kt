package com.deange.nastychristmas.settings

import kotlinx.serialization.Serializable

@Serializable
data class GameSettings(
  val enforceOwnership: Boolean,
  val showUnstealableGifts: Boolean,
  val autoScrollSpeed: Int,
) {
  companion object {
    val Default = GameSettings(
      enforceOwnership = true,
      showUnstealableGifts = false,
      autoScrollSpeed = 1,
    )
  }
}
