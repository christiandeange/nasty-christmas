package com.deange.nastychristmas.round

import com.deange.nastychristmas.model.GameStats
import com.deange.nastychristmas.model.GiftOwners
import com.deange.nastychristmas.model.Player

sealed class StealingRoundOutput {
  data class ChangeGameSettings(
    val currentPlayer: Player,
    val gifts: GiftOwners,
    val stats: GameStats,
  ) : StealingRoundOutput()

  data class EndRound(
    val playerOpeningGift: Player,
    val gifts: GiftOwners,
    val stats: GameStats,
  ) : StealingRoundOutput()
}
