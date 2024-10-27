package com.deange.nastychristmas.workflow

import com.deange.nastychristmas.model.GameStats
import com.deange.nastychristmas.model.GiftOwners
import com.deange.nastychristmas.model.Player
import com.deange.nastychristmas.settings.GameSettings
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("AppState")
sealed class AppState {
  @Serializable
  @SerialName("InitializingPlayers")
  data class InitializingPlayers(
    val allPlayers: List<Player>,
  ) : AppState()

  @Serializable
  @SerialName("PickingPlayer")
  data class PickingPlayer(
    val allPlayers: List<Player>,
    val playerPool: Set<Player>,
    val selectedPlayer: Player?,
    val round: Int,
    val gifts: GiftOwners,
    val stats: GameStats,
    val settings: GameSettings,
  ) : AppState()

  @Serializable
  @SerialName("OpeningGift")
  data class OpeningGift(
    val allPlayers: List<Player>,
    val playerPool: Set<Player>,
    val player: Player,
    val round: Int,
    val gifts: GiftOwners,
    val stats: GameStats,
    val settings: GameSettings,
  ) : AppState()

  @Serializable
  @SerialName("StealingRound")
  data class StealingRound(
    val allPlayers: List<Player>,
    val playerPool: Set<Player>,
    val startingPlayer: Player,
    val round: Int,
    val gifts: GiftOwners,
    val stats: GameStats,
    val settings: GameSettings,
  ) : AppState()

  @Serializable
  @SerialName("EndGame")
  data class EndGame(
    val gifts: GiftOwners,
    val stats: GameStats,
  ) : AppState()

  @Serializable
  @SerialName("ChangeGameSettings")
  data class ChangeGameSettings(
    val allPlayers: List<Player>,
    val playerPool: Set<Player>,
    val player: Player,
    val round: Int,
    val gifts: GiftOwners,
    val stats: GameStats,
    val settings: GameSettings,
  ) : AppState()
}
