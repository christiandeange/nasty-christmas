package com.deange.nastychristmas.round

import com.deange.nastychristmas.model.Player

data class NewRoundProps(
  val allPlayers: List<Player>,
  val playerPool: Set<Player>,
  val selectedPlayer: Player?,
  val seed: Int,
  val roundNumber: Int,
  val isReadOnly: Boolean,
)
