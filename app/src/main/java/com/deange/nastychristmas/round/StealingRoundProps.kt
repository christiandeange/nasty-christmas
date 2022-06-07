package com.deange.nastychristmas.round

import com.deange.nastychristmas.model.GiftOwners
import com.deange.nastychristmas.model.Player

data class StealingRoundProps(
  val allPlayers: List<Player>,
  val playerPool: Set<Player>,
  val roundNumber: Int,
  val startingPlayer: Player,
  val gifts: GiftOwners,
)
