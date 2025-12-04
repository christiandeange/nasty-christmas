package com.deange.nastychristmas.state

import com.deange.nastychristmas.workflow.AppState
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class GameState(
  val appState: AppState,
  val updatedAt: Instant,
)
