package com.deange.nastychristmas.state

import com.deange.nastychristmas.workflow.AppState
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class GameState(
  val appState: AppState,
  val updatedAt: Instant,
) {
  val isGameActive: Boolean
    get() = when (appState) {
      is AppState.InitializingPlayers -> false
      is AppState.PickingPlayer,
      is AppState.OpeningGift,
      is AppState.StealingRound,
      is AppState.EndGame,
      is AppState.ChangeGameSettings -> true
    }
}
