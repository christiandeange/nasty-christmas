package com.deange.nastychristmas.storage

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
  val settings: GameSettings,
)
