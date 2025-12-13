package com.deange.nastychristmas.round

import com.deange.nastychristmas.model.Player
import com.deange.nastychristmas.settings.GameSettings

data class NewRoundProps(
  val allPlayers: List<Player>,
  val playerPool: Set<Player>,
  val selectedPlayer: Player?,
  val seed: Int,
  val roundNumber: Int,
  val isReadOnly: Boolean,
  val settings: GameSettings,
)
