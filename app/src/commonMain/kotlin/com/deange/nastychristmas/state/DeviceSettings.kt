package com.deange.nastychristmas.state

import kotlinx.serialization.Serializable

@Serializable
data class DeviceSettings(
  val showUnstealableGifts: Boolean,
  val autoScrollSpeed: Int,
) {
  companion object {
    val Default = DeviceSettings(
      showUnstealableGifts = false,
      autoScrollSpeed = 1,
    )
  }
}
