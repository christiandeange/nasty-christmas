package com.deange.nastychristmas.registry

import com.deange.nastychristmas.firebase.CurrentStateSnapshot
import com.deange.nastychristmas.model.OwnedGift
import com.deange.nastychristmas.model.Player
import kotlinx.serialization.Serializable

@Serializable
data class RegistryState(
  val currentStateData: CurrentStateSnapshot?,
  val gifts: Map<Player, OwnedGift>,
  val showUnstealableGifts: Boolean,
  val autoScrollSpeed: Int,
)
