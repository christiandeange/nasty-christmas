package com.deange.nastychristmas.round

import com.deange.nastychristmas.model.Player
import kotlinx.serialization.Serializable

@Serializable
sealed class NewRoundState {
  @Serializable
  object SelectingNextPlayer : NewRoundState()

  @Serializable
  data class NextPlayerSelected(
    val player: Player,
  ) : NewRoundState()
}
