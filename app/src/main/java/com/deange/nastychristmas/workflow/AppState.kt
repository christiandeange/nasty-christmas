package com.deange.nastychristmas.workflow

import com.deange.nastychristmas.model.Player
import kotlinx.serialization.Serializable

@Serializable
sealed class AppState {
  @Serializable
  object InitializingPlayers : AppState()

  @Serializable
  data class PickingPlayer(
    val allPlayers: List<Player>,
    val playerPool: Set<Player>,
    val round: Int,
  ): AppState()
}
