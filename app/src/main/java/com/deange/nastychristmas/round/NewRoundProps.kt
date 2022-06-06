package com.deange.nastychristmas.round

import com.deange.nastychristmas.model.Player

data class NewRoundProps(
  val allPlayers: List<Player>,
  val playerPool: Set<Player>,
  val roundNumber: Int,
)
