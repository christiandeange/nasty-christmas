package com.deange.nastychristmas.workflow

import com.deange.nastychristmas.state.GameState

sealed class AppOutput {
  data object Exit : AppOutput()

  data object ClearGameState : AppOutput()

  data class SaveGameState(
    val gameState: GameState,
  ) : AppOutput()
}
