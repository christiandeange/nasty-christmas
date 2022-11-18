package com.deange.nastychristmas.workflow

import com.deange.nastychristmas.state.GameState

sealed class AppOutput {
  object Exit : AppOutput()

  object ClearGameState : AppOutput()

  data class SaveGameState(
    val gameState: GameState,
  ) : AppOutput()
}
