package com.deange.nastychristmas.firebase

import kotlinx.serialization.Serializable

@Serializable
class CurrentStateSnapshot(
  val player: String?,
  val round: Int?,
) {
  val id: String
    get() = "default"
}
