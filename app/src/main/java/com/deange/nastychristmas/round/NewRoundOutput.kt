package com.deange.nastychristmas.round

import com.deange.nastychristmas.model.Player

sealed class NewRoundOutput {
  data class UpdateGameStateWithPlayer(
    val player: Player,
  ) : NewRoundOutput()

  data class PlayerSelected(
    val player: Player,
  ) : NewRoundOutput()
}
