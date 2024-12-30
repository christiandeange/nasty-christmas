package com.deange.nastychristmas.state

import com.deange.nastychristmas.workflow.AppState
import kotlinx.datetime.Clock

class GameStateFactory(
  private val clock: Clock,
) {
  fun create(appState: AppState): GameState {
    return GameState(
      appState = appState,
      updatedAt = clock.now(),
    )
  }
}
