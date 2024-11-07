package com.deange.nastychristmas.state

import com.deange.nastychristmas.workflow.AppState
import kotlinx.serialization.Serializable

@Serializable
data class GameState(
  val appState: AppState,
)
