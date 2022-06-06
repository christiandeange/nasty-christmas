package com.deange.nastychristmas.workflow

import kotlinx.serialization.Serializable

@Serializable
sealed class AppState {
  @Serializable
  object InitializingPlayers : AppState()
}
