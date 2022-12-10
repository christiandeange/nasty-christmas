package com.deange.nastychristmas.state

import com.deange.nastychristmas.model.GameStats
import com.deange.nastychristmas.model.GiftOwners
import com.deange.nastychristmas.model.Player
import com.deange.nastychristmas.settings.GameSettings
import kotlinx.serialization.Serializable

@Serializable
data class GameState(
  val allPlayers: List<Player>,
  val playerPool: Set<Player>,
  val roundNumber: Int,
  val currentPlayer: Player?,
  val gifts: GiftOwners,
  val stats: GameStats,
  val settings: GameSettings,
)
