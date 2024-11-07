package com.deange.nastychristmas.workflow

import com.deange.nastychristmas.state.GameState

sealed class AppProps {
  abstract val isReadOnly: Boolean

  data object NewGame : AppProps() {
    override val isReadOnly: Boolean = false
  }

  data class RestoredFromSave(
    val gameState: GameState,
    override val isReadOnly: Boolean,
  ) : AppProps()
}
