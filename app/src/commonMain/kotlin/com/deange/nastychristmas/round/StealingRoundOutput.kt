package com.deange.nastychristmas.round

import com.deange.nastychristmas.model.GiftOwners
import com.deange.nastychristmas.model.Player

sealed class StealingRoundOutput {
  data class ChangeGameSettings(
    val currentPlayer: Player,
    val gifts: GiftOwners,
  ) : StealingRoundOutput()

  data class EndRound(
    val playerOpeningGift: Player,
    val gifts: GiftOwners,
  ) : StealingRoundOutput()
}
