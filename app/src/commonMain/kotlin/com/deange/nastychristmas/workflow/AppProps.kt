package com.deange.nastychristmas.workflow

import com.deange.nastychristmas.state.GameState

sealed class AppProps {
  object NewGame : AppProps()

  data class RestoredFromSave(
    val gameState: GameState,
  ) : AppProps()
}
